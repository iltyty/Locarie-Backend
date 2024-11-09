package com.locarie.backend.repositories.user;

import com.locarie.backend.domain.entities.UserEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  boolean existsByEmail(String email);

  List<UserEntity> findByType(UserEntity.Type type);

  @Query(
      value =
          "select * from users u where u.type = 'BUSINESS' order by ST_DISTANCE_SPHERE(u.location,"
              + " Point(:longitude, :latitude)), u.id",
      nativeQuery = true)
  Page<UserEntity> listBusinesses(double latitude, double longitude, Pageable pageable);

  Optional<UserEntity> findByEmail(String email);

  @Query(
      value =
          "select count(u) from UserEntity u join u.favoredBy b where u.id = :businessId and b.id ="
              + " :userId")
  int hasBeenFollowed(Long userId, Long businessId);

  @Modifying
  @Query(value = "update UserEntity u set u.password = :password where u.email = :email")
  void updatePassword(String email, String password);

  boolean existsByEmailAndPassword(String email, String password);
}
