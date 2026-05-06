# ImageKit.io Integration Guide

This document explains how to configure and use ImageKit.io for image storage in the Nexus Retail Backend application.

## Overview

The application supports multiple storage providers for file uploads:
- **Local Storage**: Files stored on the local filesystem (default for development)
- **ImageKit.io**: Cloud-based image storage and optimization service

## Configuration

### Environment Variables

Set the following environment variables for ImageKit configuration:

```bash
# Required for ImageKit
IMAGEKIT_PUBLIC_KEY=your_public_key_here
IMAGEKIT_PRIVATE_KEY=your_private_key_here
IMAGEKIT_URL_ENDPOINT=https://ik.imagekit.io/your_imagekit_id
```

### Application Properties

The storage provider is configured via `app.upload.provider`:

```properties
# Use local storage (default)
app.upload.provider=local

# Use ImageKit
app.upload.provider=imagekit
```

### Profile-Specific Configuration

- **Development (`application-dev.properties`)**: Uses local storage by default
- **UAT (`application-uat.properties`)**: Uses ImageKit by default
- **Production**: Configure according to your deployment needs

## ImageKit Setup

### 1. Create ImageKit Account

1. Sign up at [ImageKit.io](https://imagekit.io)
2. Create a new project or use existing one

### 2. Get API Credentials

1. Go to **Developer Options** > **API Keys** in your ImageKit dashboard
2. Copy the following values:
   - **Public Key**: Used for client-side operations
   - **Private Key**: Used for server-side operations (keep secret!)
   - **URL Endpoint**: Your ImageKit URL (e.g., `https://ik.imagekit.io/your_imagekit_id`)

### 3. Configure Environment Variables

Set the environment variables in your deployment environment:

```bash
export IMAGEKIT_PUBLIC_KEY="your_public_key"
export IMAGEKIT_PRIVATE_KEY="your_private_key"
export IMAGEKIT_URL_ENDPOINT="https://ik.imagekit.io/your_imagekit_id"
```

### 4. Folder Structure

Images are organized in the following folder structure:
```
/nexusretail/profiles/  # Employee profile pictures
```

## Switching Between Storage Providers

### For Development
Use local storage to avoid cloud costs during development:

```properties
app.upload.provider=local
```

### For Production
Use ImageKit for optimized image delivery:

```properties
app.upload.provider=imagekit
```

## API Usage

The file upload API remains the same regardless of the storage provider:

```http
POST /api/v1/employees/upload-profile-picture
Content-Type: multipart/form-data

# Form data:
file: [image file]
```

## Features

### Automatic Optimization
ImageKit automatically optimizes images for web delivery, including:
- Format conversion (WebP, AVIF)
- Compression
- Responsive images
- CDN delivery

### Security
- Images are stored in private folders
- URLs are generated securely
- File validation (type and size limits)

### Easy Migration
The `FileUploadUtil` class is designed for easy addition of new storage providers:
- Add new case in `uploadFile()` method
- Implement provider-specific upload/delete methods
- Update configuration properties

## Troubleshooting

### Common Issues

1. **"ImageKit is not configured"**
   - Check that all ImageKit environment variables are set
   - Verify the values are correct in ImageKit dashboard

2. **Upload fails**
   - Check file size (max 5MB)
   - Verify file type (images only)
   - Check ImageKit account limits

3. **Images not loading**
   - Verify URL endpoint configuration
   - Check folder permissions in ImageKit
   - Ensure images were uploaded successfully

### Logs

Check application logs for detailed error messages:
- Upload success/failure logs
- ImageKit API errors
- File validation errors

## Future Enhancements

### Additional Storage Providers
To add support for other providers (AWS S3, Cloudinary, etc.):

1. Add dependency to `build.gradle`
2. Create configuration class similar to `ImageKitConfig`
3. Add provider-specific methods to `FileUploadUtil`
4. Update configuration properties

### Image Processing
- Resize images for different use cases
- Generate thumbnails
- Apply watermarks
- Convert formats

## Support

For ImageKit-specific issues:
- Check [ImageKit Documentation](https://docs.imagekit.io/)
- Contact ImageKit support

For application-specific issues:
- Check application logs
- Verify configuration
- Test with local storage first
