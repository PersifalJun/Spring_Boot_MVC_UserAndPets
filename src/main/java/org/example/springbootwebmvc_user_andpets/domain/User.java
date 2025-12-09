package org.example.springbootwebmvc_user_andpets.domain;

import jakarta.validation.constraints.*;

import java.util.List;

public record User(
        Long id,
        @NotBlank(message = "User's name mustn't be blank")
        String name,
        @NotBlank(message = "User's email mustn't be blank")
        @Email
        String email,
        @Min(value = 0, message = "Age must be equal or more than 0")
        @Max(value = 100, message = "In most cases people can't live more than 100 years")
        @NotNull(message = "User's age mustn't be blank")
        Integer age,
        @NotNull(message = "User's pets mustn't be null")
        List<Pet> pets
) {
    @Override
    public String toString() {
        return "UserDto[id=%d, name=%s, age=%d, petsCount=%d]".formatted(
                id, name, age, pets != null ? pets.size() : 0
        );
    }
}

