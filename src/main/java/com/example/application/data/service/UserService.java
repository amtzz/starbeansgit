package com.example.application.data.service;

import com.example.application.data.entity.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<UserData> get(UUID id) {
        return repository.findById(id);
    }

    public Optional<UserData> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<UserData> findByUsername(String username, String password) {
        return repository.findByUsername(username, password);
    }

    public UserData update(UserData entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<UserData> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
