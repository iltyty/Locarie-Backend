package com.locarie.backend.storage;

import com.locarie.backend.storage.exceptions.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
  void init();

  Path store(MultipartFile file, String dirname) throws StorageException;

  Path load(String filename);

  void deleteAll();
}
