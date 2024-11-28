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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  boolean existsByEmail(String email);

  List<UserEntity> findByType(UserEntity.Type type);

  @Query(
      value =
          "select * from users u where u.type = 'BUSINESS' and u.business_name like :name order by"
              + " ST_DISTANCE_SPHERE(u.location, Point(:longitude, :latitude)), u.id",
      nativeQuery = true)
  Page<UserEntity> listBusinesses(
      @Param(value = "latitude") double latitude,
      @Param(value = "longitude") double longitude,
      @Param(value = "name") String name,
      Pageable pageable);

  @Query(value = "select u.favoredBy from UserEntity u where u.id = :businessId")
  Page<UserEntity> listFavoredBy(@Param(value = "businessId") Long businessId, Pageable pageable);

  @Query(value = "select u.favoriteBusinesses from UserEntity u where u.id = :userId")
  Page<UserEntity> listFavoriteBusinesses(@Param(value = "userId") Long userId, Pageable pageable);

  @Query(value = "select count(u.id) from UserEntity u where u.id = :businessId")
  int countFavoredBy(@Param(value = "businessId") Long businessId);

  @Query(value = "select count(u.id) from UserEntity u where u.id = :userId")
  int countFavoriteBusinesses(@Param(value = "userId") Long userId);

  Optional<UserEntity> findByEmail(String email);

  @Query(
      value =
          "select count(u) from UserEntity u join u.favoredBy b where u.id = :businessId and b.id ="
              + " :userId")
  int hasBeenFollowed(
      @Param(value = "userId") Long userId, @Param(value = "businessId") Long businessId);

  @Modifying
  @Query(value = "update UserEntity u set u.password = :password where u.email = :email")
  void updatePassword(
      @Param(value = "email") String email, @Param(value = "password") String password);

  boolean existsByEmailAndPassword(String email, String password);
}
