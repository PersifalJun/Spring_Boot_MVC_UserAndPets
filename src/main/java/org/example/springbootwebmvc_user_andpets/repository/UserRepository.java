package org.example.springbootwebmvc_user_andpets.repository;

import org.example.springbootwebmvc_user_andpets.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository {

    private final List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public void save(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> findById(Long userId) {
        return users.stream().filter(Objects::nonNull).
                filter(user -> Objects.equals(user.id(), userId)).findFirst();
    }

    public void delete(User user) {
        users.remove(user);
    }
}
