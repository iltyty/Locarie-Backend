package com.locarie.backend.repositories.redis;

import com.locarie.backend.domain.redis.ResetPasswordEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordEntryRepository extends CrudRepository<ResetPasswordEntry, String> {}
