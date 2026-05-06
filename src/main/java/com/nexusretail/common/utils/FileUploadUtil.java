package com.nexusretail.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class for handling file uploads with support for multiple storage providers
 * Currently supports: Local Storage and ImageKit.io (REST API)
 * Designed to be easily extensible for other cloud storage services
 */
@Component
@Slf4j
public class FileUploadUtil {

    private final WebClient webClient;

    @Value("${app.upload.dir:${user.home}/nexusretail/uploads}")
    private String uploadDir;

    @Value("${app.upload.base-url:http://localhost:8080/uploads}")
    private String baseUrl;

    @Value("${app.upload.provider:local}")
    private String uploadProvider;

    @Value("${imagekit.endpoint:}")
    private String imageKitEndpoint;

    @Value("${imagekit.public-key:}")
    private String imageKitPublicKey;

    @Value("${imagekit.private-key:}")
    private String imageKitPrivateKey;

    @Value("${imagekit.folder:/profiles}")
    private String imageKitFolder;

    public FileUploadUtil(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Upload a file using the configured storage provider
     * @param file the multipart file to upload
     * @param subfolder optional subfolder (e.g., "profiles", "documents")
     * @return the URL path to access the uploaded file
     * @throws IOException if file upload fails
     */
    public String uploadFile(MultipartFile file, String subfolder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Validate file type
        validateFileType(file);

        switch (uploadProvider.toLowerCase()) {
            case "imagekit":
                return uploadToImageKit(file, subfolder);
            case "local":
            default:
                return uploadToLocal(file, subfolder);
        }
    }

    /**
     * Upload a profile picture (convenience method)
     * @param file the profile picture file
     * @return the URL path to access the uploaded profile picture
     * @throws IOException if upload fails
     */
    public String uploadProfilePicture(MultipartFile file) throws IOException {
        return uploadFile(file, "profiles");
    }

    /**
     * Delete a file from storage
     * @param fileUrl the URL of the file to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            switch (uploadProvider.toLowerCase()) {
                case "imagekit":
                    return deleteFromImageKit(fileUrl);
                case "local":
                default:
                    return deleteFromLocal(fileUrl);
            }
        } catch (Exception e) {
            log.error("Error deleting file: {}", fileUrl, e);
            return false;
        }
    }

    /**
     * Upload file to ImageKit.io using REST API
     */
    private String uploadToImageKit(MultipartFile file, String subfolder) throws IOException {
        if (imageKitEndpoint.isEmpty() || imageKitPrivateKey.isEmpty()) {
            throw new IllegalStateException("ImageKit is not configured. Please check your configuration properties.");
        }

        try {
            // Convert file to base64
            byte[] fileBytes = file.getBytes();
            String base64File = Base64.getEncoder().encodeToString(fileBytes);

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = generateUniqueFilename(fileExtension);

            // Create multipart form data
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("file", base64File);
            bodyBuilder.part("fileName", uniqueFilename);
            bodyBuilder.part("folder", subfolder != null ? imageKitFolder + "/" + subfolder : imageKitFolder);

            // Make API call to ImageKit
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri(imageKitEndpoint + "/files/upload")
                    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((imageKitPrivateKey + ":").getBytes()))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("url")) {
                String fileUrl = (String) response.get("url");
                log.info("File uploaded to ImageKit successfully: {} -> {}", originalFilename, fileUrl);
                return fileUrl;
            } else {
                throw new IOException("ImageKit upload failed: Invalid response");
            }

        } catch (Exception e) {
            log.error("Error uploading to ImageKit: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file to ImageKit: " + e.getMessage(), e);
        }
    }

    /**
     * Upload file to local storage
     */
    private String uploadToLocal(MultipartFile file, String subfolder) throws IOException {
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = generateUniqueFilename(fileExtension);

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir, subfolder != null ? subfolder : "");
        Files.createDirectories(uploadPath);

        // Full path for the file
        Path filePath = uploadPath.resolve(uniqueFilename);

        // Copy file to destination
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the accessible URL
        String relativePath = subfolder != null ? subfolder + "/" + uniqueFilename : uniqueFilename;
        String fileUrl = baseUrl + "/" + relativePath;

        log.info("File uploaded to local storage successfully: {} -> {}", originalFilename, fileUrl);
        return fileUrl;
    }

    /**
     * Delete file from ImageKit.io using REST API
     */
    private boolean deleteFromImageKit(String fileUrl) {
        if (imageKitEndpoint.isEmpty() || imageKitPrivateKey.isEmpty()) {
            log.warn("ImageKit is not configured, cannot delete file: {}", fileUrl);
            return false;
        }

        try {
            // Extract file ID from URL
            String fileId = extractFileIdFromImageKitUrl(fileUrl);
            if (fileId == null) {
                log.warn("Could not extract file ID from ImageKit URL: {}", fileUrl);
                return false;
            }

            // Make API call to delete file
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.delete()
                    .uri(imageKitEndpoint + "/files/" + fileId)
                    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((imageKitPrivateKey + ":").getBytes()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            boolean deleted = response != null && "success".equalsIgnoreCase((String) response.get("message"));

            if (deleted) {
                log.info("File deleted from ImageKit successfully: {}", fileUrl);
            } else {
                log.warn("Failed to delete file from ImageKit: {}", fileUrl);
            }

            return deleted;

        } catch (Exception e) {
            log.error("Error deleting file from ImageKit: {}", fileUrl, e);
            return false;
        }
    }

    /**
     * Delete file from local storage
     */
    private boolean deleteFromLocal(String fileUrl) {
        try {
            // Extract relative path from URL
            String relativePath = extractRelativePathFromUrl(fileUrl);
            Path filePath = Paths.get(uploadDir, relativePath);

            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                log.info("File deleted from local storage successfully: {}", fileUrl);
            } else {
                log.warn("File not found for deletion: {}", fileUrl);
            }

            return deleted;
        } catch (Exception e) {
            log.error("Error deleting file from local storage: {}", fileUrl, e);
            return false;
        }
    }

    /**
     * Validate file type (basic validation for common image types)
     * @param file the file to validate
     */
    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("File type cannot be determined");
        }

        // Allow common image formats
        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Additional size validation (max 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 5MB");
        }
    }

    /**
     * Generate a unique filename using UUID
     * @param fileExtension the file extension (e.g., "jpg", "png")
     * @return unique filename
     */
    private String generateUniqueFilename(String fileExtension) {
        return UUID.randomUUID().toString() + "." + fileExtension;
    }

    /**
     * Get file extension from filename
     * @param filename the original filename
     * @return file extension (lowercase)
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "jpg"; // default extension
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    /**
     * Extract relative path from full URL (for local storage)
     * @param fileUrl the full file URL
     * @return relative path from upload directory
     */
    private String extractRelativePathFromUrl(String fileUrl) {
        if (fileUrl.startsWith(baseUrl)) {
            return fileUrl.substring(baseUrl.length() + 1); // +1 for the "/"
        }
        // If URL doesn't match base URL, assume it's already a relative path
        return fileUrl;
    }

    /**
     * Extract file ID from ImageKit URL
     * ImageKit URLs typically look like: https://ik.imagekit.io/your_imagekit_id/image_name.jpg
     * We need to extract the file ID which is usually the filename without extension
     */
    private String extractFileIdFromImageKitUrl(String fileUrl) {
        try {
            // Remove query parameters if any
            int queryIndex = fileUrl.indexOf('?');
            if (queryIndex > 0) {
                fileUrl = fileUrl.substring(0, queryIndex);
            }

            // Get the last part of the URL (filename)
            int lastSlashIndex = fileUrl.lastIndexOf('/');
            if (lastSlashIndex >= 0 && lastSlashIndex < fileUrl.length() - 1) {
                String filename = fileUrl.substring(lastSlashIndex + 1);

                // Remove file extension to get file ID
                int dotIndex = filename.lastIndexOf('.');
                if (dotIndex > 0) {
                    return filename.substring(0, dotIndex);
                }
                return filename;
            }
        } catch (Exception e) {
            log.error("Error extracting file ID from ImageKit URL: {}", fileUrl, e);
        }
        return null;
    }

    // Getters for configuration (useful for testing or other components)
    public String getUploadDir() {
        return uploadDir;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUploadProvider() {
        return uploadProvider;
    }
}
