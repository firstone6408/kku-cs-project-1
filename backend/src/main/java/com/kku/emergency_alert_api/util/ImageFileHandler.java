package com.kku.emergency_alert_api.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * ImageFileHandler - A utility class for handling image uploads, replacements,
 * and deletions.
 * It automatically resizes images and saves them to a specified folder.
 *
 * ## Example Usage:
 * ImageFileHandler handler = new ImageFileHandler("products");
 * String path = handler.uploadFile(file);
 */
public class ImageFileHandler {
    private final Path uploadDir;
    private final String folderName;

    /**
     * Constructor to initialize the upload directory.
     *
     * @param folderName - The folder where the images will be stored.
     */
    public ImageFileHandler(String folderName) {
        this.folderName = folderName;
        this.uploadDir = Paths.get("src/main/resources/static", "uploads", folderName);

        try {
            if (!Files.exists(this.uploadDir)) {
                Files.createDirectories(this.uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory", e);
        }
    }

    /**
     * Uploads a file, resizes it, and saves it to the server.
     *
     * @param file - The uploaded file (MultipartFile).
     * @return The relative URL path of the uploaded file.
     */
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Invalid file provided");
        }

        try {
            // Unique name
            String uniqueName = UUID.randomUUID().toString() + ".jpg";
            Path targetPath = this.uploadDir.resolve(uniqueName);

            // Resize and convert to webp (Thumbnailator auto detects format by extension)
            Thumbnails.of(file.getInputStream())
                    .size(800, 800)
                    .outputFormat("jpg")
                    .outputQuality(0.8) // 80%
                    .toFile(targetPath.toFile());

            return "/uploads/" + this.folderName + "/" + uniqueName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    /**
     * Replaces an existing file by deleting the old file and uploading a new one.
     *
     * @param oldFilePath - The path of the existing file to be replaced.
     * @param newFile     - The new uploaded file.
     * @return The relative URL path of the new file.
     */
    public String replaceFile(String oldFilePath, MultipartFile newFile) {
        deleteFile(oldFilePath);
        return uploadFile(newFile);
    }

    /**
     * Deletes an existing file from the upload directory.
     *
     * @param filePath - The path of the file to be deleted.
     * @return true if deleted, false otherwise.
     */
    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        try {
            String cleanedFileName = Paths.get(filePath).getFileName().toString();
            Path targetPath = this.uploadDir.resolve(cleanedFileName);

            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
