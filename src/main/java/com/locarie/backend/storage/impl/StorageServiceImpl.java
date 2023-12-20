package com.locarie.backend.storage.impl;

import com.locarie.backend.storage.StorageService;
import com.locarie.backend.storage.config.StorageConfig;
import com.locarie.backend.storage.exceptions.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class StorageServiceImpl implements StorageService {

  private final Path rootLocation;

  public StorageServiceImpl(StorageConfig properties) throws RuntimeException {
    String rootLocation = properties.getLocation();
    checkRootLocationEmptyAndThrows(rootLocation);
    this.rootLocation = Path.of(rootLocation);
    init();
  }

  private void checkRootLocationEmptyAndThrows(String rootLocation) {
    if (rootLocation.trim().isEmpty()) {
      throw new RuntimeException("Storage location is not set");
    }
  }

  @Override
  public void init() {
    try {
      createRootDirectory();
    } catch (IOException e) {
      handleCreateRootDirectoryException(e);
    }
  }

  private void createRootDirectory() throws IOException {
    Files.createDirectories(rootLocation);
  }

  private void handleCreateRootDirectoryException(IOException e) throws StorageException {
    throw new StorageException("Could not initialize storage", e);
  }

  @Override
  public Path store(MultipartFile file, String dirname) throws StorageException {
    checkInvalidFile(file);
    assert file.getOriginalFilename() != null;
    Path fileRootLocation = resolveFileSpecificRootLocation(dirname);
    try {
      createFileSpecificRootDirectoryAndThrows(fileRootLocation);
      Path destinationFile = resolveFilePath(file.getOriginalFilename(), fileRootLocation);
      checkDestinationFileOutsideCurrentDirectory(destinationFile, fileRootLocation);
      doStore(file, destinationFile);
      return destinationFile;
    } catch (IOException e) {
      throw handleIOException(e);
    }
  }

  private void checkInvalidFile(MultipartFile file) throws StorageException {
    checkFileEmpty(file);
    checkFileNameEmpty(file);
  }

  private Path resolveFileSpecificRootLocation(String dirname) {
    return rootLocation.resolve(dirname);
  }

  private void createFileSpecificRootDirectoryAndThrows(Path rootLocation) throws IOException {
    Files.createDirectories(rootLocation);
  }

  private Path resolveFilePath(String filename, Path rootLocation) {
    Path filePath = Path.of(filename);
    return rootLocation.resolve(filePath).normalize().toAbsolutePath();
  }

  private void checkDestinationFileOutsideCurrentDirectory(
      Path destinationFile, Path fileRootLocation) throws StorageException {
    if (!destinationFile.getParent().equals(fileRootLocation.toAbsolutePath())) {
      throw new StorageException("Cannot store file outside current directory");
    }
  }

  private void doStore(MultipartFile file, Path destinationFile) throws IOException {
    try (InputStream inputStream = file.getInputStream()) {
      Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private StorageException handleIOException(IOException e) throws StorageException {
    throw new StorageException("Failed to store file", e);
  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }

  private void checkFileEmpty(MultipartFile file) throws StorageException {
    if (file.isEmpty()) {
      throw new StorageException("Failed to store empty file");
    }
  }

  private void checkFileNameEmpty(MultipartFile file) throws StorageException {
    if (file.getOriginalFilename() == null) {
      throw new StorageException("Failed to store file without a name");
    }
  }
}
