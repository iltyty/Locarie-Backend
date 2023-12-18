package com.locarie.backend.storage.utils;

public class StorageUtil {
    public static String getUserAvatarDirname(Long userId) {
        return String.format("user_%d/avatar", userId);
    }

    public static String getPostImagesDirname(Long userId, Long postId) {
        return String.format("user_%d/posts/post_%d", userId, postId);
    }
}
