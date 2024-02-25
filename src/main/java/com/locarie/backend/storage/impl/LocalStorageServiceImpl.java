package com.locarie.backend.storage.impl;

import com.locarie.backend.storage.StorageService;
import com.locarie.backend.storage.config.StorageConfig;
import com.locarie.backend.storage.exceptions.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(value = "storage.type", havingValue = "local", matchIfMissing = true)
public class LocalStorageServiceImpl implements StorageService {
  private final String urlPrefix;
  private final Path rootLocation = Path.of("static");

  public LocalStorageServiceImpl(StorageConfig properties) throws RuntimeException {
    this.urlPrefix = properties.getUrlPrefix();
    init();
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
  public Path getUserDataDirPath(Long id) {
    return rootLocation.resolve("user_" + id);
  }

  @Override
  public String store(MultipartFile file, String dirname) throws StorageException {
    checkInvalidFile(file);
    assert file.getOriginalFilename() != null;
    Path fileRootLocation = resolveFileSpecificRootLocation(dirname);
    try {
      createFileSpecificRootDirectoryAndThrows(fileRootLocation);
      Path destinationFile = resolveFilePath(file.getOriginalFilename(), fileRootLocation);
      checkDestinationFileOutsideCurrentDirectory(destinationFile, fileRootLocation);
      doStore(file, destinationFile);
      return concatenateAvatarUrl(dirname, file);
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

  private String concatenateAvatarUrl(String dirname, MultipartFile file) {
    return urlPrefix + Paths.get(dirname, file.getOriginalFilename());
  }

  private StorageException handleIOException(IOException e) throws StorageException {
    throw new StorageException("Failed to store file", e);
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

  @Override
  public void deleteUserDataDir(Long id) {
    Path userDir = rootLocation.resolve("user_" + id);
    FileSystemUtils.deleteRecursively(userDir.toFile());
  }
}
