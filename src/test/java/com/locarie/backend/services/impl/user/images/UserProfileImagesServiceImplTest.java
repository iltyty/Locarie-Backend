package com.locarie.backend.services.impl.user.images;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.locarie.backend.datacreators.image.MockImageCreator;
import com.locarie.backend.datacreators.user.UserEntityCreator;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.impl.user.UserProfileImagesServiceImpl;
import com.locarie.backend.storage.exceptions.StorageException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserProfileImagesServiceImplTest {
  @Autowired private UserProfileImagesServiceImpl underTests;
  @Autowired private UserRepository userRepository;

  @Test
  void testUploadShouldSucceed() {
    Long userId = givenUserIdAfterCreated();
    MockMultipartFile[] profileImages = givenProfileImages();
    List<String> result = whenUploadProfileImages(userId, profileImages);
    thenResultShouldBeOfSize(result, profileImages.length);
    thenResultShouldContainImageUrls(result, profileImages);
  }

  @Test
  void testUploadProfileImagesWithoutFilenameShouldFail() {
    Long userId = givenUserIdAfterCreated();
    MockMultipartFile[] profileImages = givenProfileImagesWithoutFilename();
    thenResultShouldThrowStorageExceptionWhenUploadProfileImages(userId, profileImages);
  }

  private Long givenUserIdAfterCreated() {
    UserEntity userEntity = UserEntityCreator.businessUserEntityJoleneHornsey();
    return userRepository.save(userEntity).getId();
  }

  private MockMultipartFile[] givenProfileImages() {
    return new MockMultipartFile[] {
      MockImageCreator.pngImage(), MockImageCreator.jpgImage(), MockImageCreator.jpegImage()
    };
  }

  private MockMultipartFile[] givenProfileImagesWithoutFilename() {
    return new MockMultipartFile[] {
      MockImageCreator.imageWithoutFilename(),
      MockImageCreator.imageWithoutFilename(),
      MockImageCreator.imageWithoutFilename()
    };
  }

  private List<String> whenUploadProfileImages(Long id, MockMultipartFile[] profileImages) {
    return underTests.uploadProfileImages(id, profileImages);
  }

  private void thenResultShouldBeOfSize(List<String> result, int size) {
    assertThat(result).hasSize(size);
  }

  private void thenResultShouldContainImageUrls(
      List<String> result, MockMultipartFile[] profileImages) {
    for (int i = 0; i < profileImages.length; i++) {
      assertThat(result.get(i)).contains(profileImages[i].getOriginalFilename());
    }
  }

  private void thenResultShouldThrowStorageExceptionWhenUploadProfileImages(
      Long userId, MockMultipartFile[] profileImages) {
    assertThatExceptionOfType(StorageException.class)
        .isThrownBy(
            () -> {
              whenUploadProfileImages(userId, profileImages);
            });
  }
}
