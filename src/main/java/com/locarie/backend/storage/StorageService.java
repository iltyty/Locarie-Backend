package com.locarie.backend.storage;

import com.locarie.backend.storage.exceptions.StorageException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  void init();

  String store(MultipartFile file, String dirname) throws StorageException;

  void deleteAll();
}
