package org.example.springbootwebmvc_user_andpets.converter;

import org.example.springbootwebmvc_user_andpets.domain.Pet;
import org.example.springbootwebmvc_user_andpets.dto.PetDto;
import org.springframework.stereotype.Component;

@Component
public class PetDtoConverter {
    public PetDto toPetDto(Pet pet) {
        return new PetDto(
                pet.id(),
                pet.name(),
                pet.userId()
        );
    }

    public Pet toDomain(PetDto petDto) {
        return new Pet(
                petDto.id(),
                petDto.name(),
                petDto.userId()
        );
    }
}
