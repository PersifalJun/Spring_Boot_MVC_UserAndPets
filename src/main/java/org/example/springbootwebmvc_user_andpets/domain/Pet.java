package org.example.springbootwebmvc_user_andpets.domain;

import jakarta.validation.constraints.NotBlank;

public record Pet(
        Long id,
        @NotBlank(message = "Pet's name mustn't be blank")
        String name,
        Long userId
) {
}
