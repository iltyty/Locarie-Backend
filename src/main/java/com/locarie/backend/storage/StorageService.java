package com.locarie.backend.storage;

import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();

    Path store(MultipartFile file, String dirname);

    Path load(String filename);

    void deleteAll();
}
