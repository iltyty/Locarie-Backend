package com.locarie.backend.utils;

import org.springframework.mock.web.MockMultipartFile;

public class MockAvatarCreator {
  public static MockMultipartFile pngAvatar() {
    return new MockMultipartFile("avatar", "avatar.png", "image/png", new byte[1]);
  }

  public static MockMultipartFile jpgAvatar() {
    return new MockMultipartFile("avatar", "avatar.jpg", "image/jpg", new byte[1]);
  }
}
