package com.locarie.backend.datacreators;

import org.springframework.mock.web.MockMultipartFile;

public class MockAvatarCreator {
  public static MockMultipartFile pngAvatar() {
    return new MockMultipartFile("avatar", "avatar.png", "image/png", new byte[100]);
  }

  public static MockMultipartFile jpgAvatar() {
    return new MockMultipartFile("avatar", "avatar.jpg", "image/jpg", new byte[1]);
  }

  public static MockMultipartFile emptyAvatar() {
    return new MockMultipartFile("avatar", "avatar.jpg", "image/jpg", new byte[0]);
  }

  public static MockMultipartFile avatarWithNoFilename() {
    return new MockMultipartFile("avatar", "", "image/jpg", new byte[1]);
  }
}
