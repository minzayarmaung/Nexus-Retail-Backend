package com.nexusretail.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Utility class for handling file uploads, initially for local storage
 * Designed to be easily extensible for cloud storage (S3, Cloudinary, etc.)
 */
@Component
@Slf4j
public class FileUploadUtil {

    @Value("${app.upload.dir:${user.home}/nexusretail/uploads}")
    private String uploadDir;

    @Value("${app.upload.base-url:http://localhost:8080/uploads}")
    private String baseUrl;

    /**
     * Upload a file to local storage
     * @param file the multipart file to upload
     * @param subfolder optional subfolder (e.g., "profiles", "documents")
     * @return the URL path to access the uploaded file
     * @throws IOException if file upload fails
     */
    public String uploadFile(MultipartFile file, String subfolder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Validate file type (basic validation)
        validateFileType(file);

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

        log.info("File uploaded successfully: {} -> {}", originalFilename, fileUrl);
        return fileUrl;
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
     * Delete a file from local storage
     * @param fileUrl the URL of the file to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            // Extract relative path from URL
            String relativePath = extractRelativePathFromUrl(fileUrl);
            Path filePath = Paths.get(uploadDir, relativePath);

            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                log.info("File deleted successfully: {}", fileUrl);
            } else {
                log.warn("File not found for deletion: {}", fileUrl);
            }

            return deleted;
        } catch (Exception e) {
            log.error("Error deleting file: {}", fileUrl, e);
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
     * Extract relative path from full URL
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

    // Getters for configuration (useful for testing or other components)
    public String getUploadDir() {
        return uploadDir;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
