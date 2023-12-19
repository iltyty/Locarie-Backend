package com.locarie.backend.storage.impl;

import com.locarie.backend.storage.StorageService;
import com.locarie.backend.storage.config.StorageConfig;
import com.locarie.backend.storage.exceptions.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

  private final Path rootLocation;

  public StorageServiceImpl(StorageConfig properties) {
    if (properties.getLocation().trim().isEmpty()) {
      throw new RuntimeException("Storage location is not set");
    }
    this.rootLocation = Path.of(properties.getLocation());
    init();
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

  @Override
  public Path store(MultipartFile file, String dirname) throws StorageException {
    Path newRootLocation = rootLocation.resolve(dirname);
    try {
      Files.createDirectories(newRootLocation);
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file");
      }
      if (file.getOriginalFilename() == null) {
        throw new StorageException("Failed to store file without a name");
      }
      Path destinationFile =
          newRootLocation.resolve(Path.of(file.getOriginalFilename())).normalize().toAbsolutePath();
      if (!destinationFile.getParent().equals(newRootLocation.toAbsolutePath())) {
        throw new StorageException("Cannot store file outside current directory");
      }
      try (InputStream stream = file.getInputStream()) {
        Files.copy(stream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
      }
      return destinationFile;
    } catch (IOException e) {
      throw new StorageException("Failed to store file", e);
    }
  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }
}
