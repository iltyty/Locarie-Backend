package com.locarie.backend.storage;

import com.locarie.backend.storage.exceptions.StorageException;
import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  void init();

  Path store(MultipartFile file, String dirname) throws StorageException;

  Path load(String filename);

  void deleteAll();
}
