package com.sample.springboot.view.velocity.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

    private static final String UPLOAD_DIR = "upload";

    private final Path uploadPath;

    public FileSystemStorageService() {
        init();
        this.uploadPath = Paths.get(UPLOAD_DIR);
    }

    @Override
    public void init() {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (Files.notExists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file, String... path) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }

            // 上传文件夹
            Path dirPath = Paths.get(UPLOAD_DIR, path);
            if (Files.notExists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // 文件扩展名 https://github.com/apache/commons-io
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            // 文件名UUID.ext
            String fileName = UUID.randomUUID().toString() + "." + extension;
            // 文件存储路径
            Path filePath = dirPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);
            return fileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(uploadPath, 1)
                    .filter(path -> !path.equals(uploadPath))
                    .map(uploadPath::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename, String... path) {
        return Paths.get(UPLOAD_DIR, path).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, String... location) {
        try {
            Path filePath = load(filename, location);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(uploadPath.toFile());
    }
}
