package com.locarie.backend.repositories.user;

import com.locarie.backend.domain.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
  boolean existsByEmail(String email);

  Optional<UserEntity> findByEmail(String email);

  @Query(
      value =
          "select count(u) from UserEntity u join u.favoredBy b where u.id = :businessId and b.id ="
              + " :userId")
  int hasBeenFollowed(Long userId, Long businessId);
}
