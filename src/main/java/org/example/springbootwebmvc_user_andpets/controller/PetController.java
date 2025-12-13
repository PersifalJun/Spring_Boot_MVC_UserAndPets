package org.example.springbootwebmvc_user_andpets.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootwebmvc_user_andpets.converter.PetDtoConverter;
import org.example.springbootwebmvc_user_andpets.dto.PetDto;
import org.example.springbootwebmvc_user_andpets.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("users/{ownerId}/pets")
public class PetController {

    private final PetService petService;
    private final PetDtoConverter petDtoConverter;

    public PetController(PetService petService, PetDtoConverter petDtoConverter) {
        this.petService = petService;
        this.petDtoConverter = petDtoConverter;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(
            @PathVariable Long ownerId,
            @RequestBody @Valid PetDto petToCreate
    ) {
        log.info("Post request for create pet: owner={}, pet={}", ownerId, petToCreate);
        var createdPet = petService.createPet(ownerId,
                petDtoConverter.toDomain(petToCreate));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petDtoConverter.toPetDto(createdPet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(
            @PathVariable("ownerId") Long ownerId,
            @PathVariable("id") Long petId
    ) {
        log.info("Delete request for delete pet: owner={}, id={}", ownerId, petId);
        petService.deletePet(ownerId, petId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
