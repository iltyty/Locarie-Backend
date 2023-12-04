package com.locarie.backend.repositories;

import com.locarie.backend.domain.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> emailEquals(String email);
}
