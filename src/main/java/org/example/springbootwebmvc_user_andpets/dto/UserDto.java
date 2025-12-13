package org.example.springbootwebmvc_user_andpets.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import org.example.springbootwebmvc_user_andpets.domain.Pet;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
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
        List<PetDto> pets
) {
    @Override
    public String toString() {
        return "UserDto[id=%d, name=%s, age=%d, petsCount=%d]".formatted(
                id, name, age, pets != null ? pets.size() : 0
        );
    }
}
