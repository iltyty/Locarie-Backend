package com.locarie.backend.repositories.user;

import com.locarie.backend.domain.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
  Boolean existsByEmail(String email);

  Optional<UserEntity> findByEmail(String email);
}
