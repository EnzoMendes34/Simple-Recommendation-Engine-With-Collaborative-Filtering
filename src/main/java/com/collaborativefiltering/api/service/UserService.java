package com.collaborativefiltering.api.service;

import com.collaborativefiltering.api.repository.UserRepositoryInMemory;
import com.collaborativefiltering.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepositoryInMemory repository;

    public UserService(UserRepositoryInMemory repository) {
        this.repository = repository;
    }

    public void save(User user) {
        if(user == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        repository.save(user);
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public List<String> findAllIds() {

        return repository.findAllIds();
    }

    public void delete(String id) {
        if(id == null) {
            throw new IllegalArgumentException("You must pass an id.");
        }

        repository.delete(id);
    }

    public void deleteAll(){
        repository.deleteAll();
    }


}
