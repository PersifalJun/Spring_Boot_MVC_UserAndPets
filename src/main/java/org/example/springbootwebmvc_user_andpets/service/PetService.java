package org.example.springbootwebmvc_user_andpets.service;

import org.example.springbootwebmvc_user_andpets.exception.NoPetException;
import org.example.springbootwebmvc_user_andpets.exception.NoUserException;
import org.example.springbootwebmvc_user_andpets.model.PetDto;
import org.example.springbootwebmvc_user_andpets.repository.PetRepository;
import org.example.springbootwebmvc_user_andpets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private long petIdCounter;

    @Autowired
    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petIdCounter = 0L;
    }

    private long nextPetId() {
        return ++petIdCounter;
    }

    public PetDto createPet(Long ownerId, PetDto petToCreate) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NoUserException(
                        "User with id=%s not found".formatted(ownerId)));

        var newPet = new PetDto(
                nextPetId(),
                petToCreate.name(),
                owner.id()
        );
        owner.pets().add(newPet);
        petRepository.save(ownerId, newPet);

        return newPet;
    }

    public void deletePet(Long ownerId, Long petId) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NoUserException(
                        "User with id=%s not found".formatted(ownerId)));

        var petToDelete = petRepository.findById(petId)
                .orElseThrow(() -> new NoPetException(
                        "No pet found by id = %s".formatted(petId)));

        if (!Objects.equals(petToDelete.userId(), ownerId)) {
            throw new NoPetException(
                    "Pet with id = %s doesn't belong to user with id = %s"
                            .formatted(petId, ownerId));
        }
        petRepository.delete(ownerId, petToDelete);
        owner.pets().remove(petToDelete);
    }
}
