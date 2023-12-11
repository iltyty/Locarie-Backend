package com.locarie.backend.storage;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void init();

    Path store(MultipartFile file, String dirname);

    Path load(String filename);

    void deleteAll();
}
