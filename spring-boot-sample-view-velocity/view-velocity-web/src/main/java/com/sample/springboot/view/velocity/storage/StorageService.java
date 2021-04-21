package com.sample.springboot.view.velocity.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file, String... path);

    Stream<Path> loadAll();

    Path load(String filename, String... path);

    Resource loadAsResource(String filename, String... path);

    void deleteAll();

}
