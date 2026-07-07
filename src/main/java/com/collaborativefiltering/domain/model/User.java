package com.collaborativefiltering.domain.model;

import java.util.Objects;
import java.util.UUID;

public class User {

    private String id = UUID.randomUUID().toString();

    public User() {}

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
