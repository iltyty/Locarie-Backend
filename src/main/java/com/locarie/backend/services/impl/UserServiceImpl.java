package com.locarie.backend.services.impl;

import com.locarie.backend.domain.entities.User;
import com.locarie.backend.repositories.UserRepository;
import com.locarie.backend.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }
}
