package com.locarie.backend.datacreators.image;

import org.springframework.mock.web.MockMultipartFile;

public class MockImageCreator {
  public static MockMultipartFile[] profileImages() {
    return new MockMultipartFile[] {pngImage(), jpgImage(), jpegImage()};
  }

  public static MockMultipartFile pngImage() {
    return new MockMultipartFile("images", "image1.png", "image/png", new byte[100]);
  }

  public static MockMultipartFile jpgImage() {
    return new MockMultipartFile("images", "image2.jpg", "image/jpg", new byte[1]);
  }

  public static MockMultipartFile jpegImage() {
    return new MockMultipartFile("images", "image3.jpeg", "image/jpeg", new byte[1]);
  }

  public static MockMultipartFile emptyImage() {
    return new MockMultipartFile("images", "empty.jpg", "image/jpg", new byte[0]);
  }

  public static MockMultipartFile imageWithoutFilename() {
    return new MockMultipartFile("images", "", "image/jpg", new byte[1]);
  }
}
