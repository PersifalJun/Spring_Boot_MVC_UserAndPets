package org.example.springbootwebmvc_user_andpets.repository;

import org.example.springbootwebmvc_user_andpets.model.UserDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository {

    private final List<UserDto> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public void save(UserDto user) {
        users.add(user);
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public Optional<UserDto> findById(Long userId) {
        return users.stream().filter(Objects::nonNull).
                filter(user -> Objects.equals(user.id(), userId)).findFirst();
    }
    public void delete(UserDto user) {
        users.remove(user);
    }
}
