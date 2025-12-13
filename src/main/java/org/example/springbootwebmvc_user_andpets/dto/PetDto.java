package org.example.springbootwebmvc_user_andpets.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PetDto(
        Long id,
        @NotBlank(message = "Pet's name mustn't be blank")
        String name,
        Long userId
) {
}
