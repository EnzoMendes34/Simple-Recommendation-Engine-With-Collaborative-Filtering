package com.collaborativefiltering.api.repository;

import com.collaborativefiltering.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRepositoryInMemory {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void save(User user){ users.put(user.getId(), user); }

    public Optional<User> findById(String id) { return Optional.ofNullable(users.get(id));}

    public List<User> findAll(){ return users.values().stream().toList(); }

    public List<String> findAllIds() { return users.keySet().stream().toList(); }

    public void delete(String id) { users.remove(id); }

    public void deleteAll(){ users.clear(); }

}

