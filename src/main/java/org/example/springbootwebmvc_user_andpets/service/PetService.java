package org.example.springbootwebmvc_user_andpets.service;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootwebmvc_user_andpets.domain.Pet;
import org.example.springbootwebmvc_user_andpets.exception.NoFoundOwnerPetException;
import org.example.springbootwebmvc_user_andpets.exception.NoFoundPetException;
import org.example.springbootwebmvc_user_andpets.exception.NoFoundUserException;
import org.example.springbootwebmvc_user_andpets.repository.PetRepository;
import org.example.springbootwebmvc_user_andpets.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private long petIdCounter;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petIdCounter = 0L;
    }

    private long nextPetId() {
        return ++petIdCounter;
    }

    public Pet createPet(Long ownerId, Pet petToCreate) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> {
                    log.warn("No possible to find pet owner to create pet");
                    return new NoFoundUserException(
                            "User with id=%s not found".formatted(ownerId));
                });

        var newPet = new Pet(
                nextPetId(),
                petToCreate.name(),
                owner.id()
        );
        log.info("Saving a new pet for owner");
        owner.pets().add(newPet);
        petRepository.save(ownerId, newPet);

        return newPet;
    }

    public void deletePet(Long ownerId, Long petId) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> {
                    log.warn("No possible to find pet owner to delete pet");
                    return new NoFoundUserException(
                            "User with id=%s not found".formatted(ownerId));
                });

        var petToDelete = petRepository.findById(petId)
                .orElseThrow(() -> {
                    log.warn("No possible to find pet");
                    return new NoFoundPetException(
                            "No pet found by id = %s".formatted(petId));
                });

        if (!Objects.equals(petToDelete.userId(), ownerId)) {
            log.warn("No possible to find owner's pet");
            throw new NoFoundOwnerPetException((
                    "Pet with id = %s doesn't belong to user with id = %s"
                            .formatted(petId, ownerId)));
        }
        log.info("Deleting owner's pet");
        petRepository.delete(ownerId, petToDelete);
        owner.pets().remove(petToDelete);
    }
}
