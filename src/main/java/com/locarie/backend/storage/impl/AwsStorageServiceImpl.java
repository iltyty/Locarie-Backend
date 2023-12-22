package com.locarie.backend.storage.impl;

import com.locarie.backend.storage.StorageService;
import com.locarie.backend.storage.exceptions.StorageException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(value = "storage.type", havingValue = "aws")
public class AwsStorageServiceImpl implements StorageService {
  @Override
  public void init() {}

  @Override
  public String store(MultipartFile file, String dirname) throws StorageException {
    return null;
  }

  @Override
  public void deleteAll() {}
}
