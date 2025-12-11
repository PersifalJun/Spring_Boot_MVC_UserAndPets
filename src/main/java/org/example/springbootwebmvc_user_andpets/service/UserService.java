package org.example.springbootwebmvc_user_andpets.service;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootwebmvc_user_andpets.domain.Pet;
import org.example.springbootwebmvc_user_andpets.domain.User;
import org.example.springbootwebmvc_user_andpets.exception.InvalidPetException;
import org.example.springbootwebmvc_user_andpets.exception.NoFoundUserException;
import org.example.springbootwebmvc_user_andpets.exception.NotAllowedEditException;
import org.example.springbootwebmvc_user_andpets.exception.AlreadyRegisteredException;
import org.example.springbootwebmvc_user_andpets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PetService petService;
    private long idCounter;

    @Autowired
    public UserService(UserRepository userRepository,
                       PetService petService) {
        this.userRepository = userRepository;
        this.petService = petService;
        this.idCounter = 0L;
    }

    public User createUser(User userToCreate) {
        log.info("Creating a user");
        User newUser = null;
        if (!isRegistered(userToCreate.email())) {
            var newUserId = ++idCounter;

            newUser = new User(
                    newUserId,
                    userToCreate.name(),
                    userToCreate.email(),
                    userToCreate.age(),
                    new ArrayList<>()
            );
            log.info("Saving a user");
            userRepository.save(newUser);
            log.info("Set pets to new user");
            addPetsToNewUser(userToCreate, newUserId);
        }
        return newUser;
    }

    private void addPetsToNewUser(User owner, Long userId) {
        for (Pet petFromRequest : owner.pets()) {
            petService.createPet(userId, petFromRequest);
        }
    }

    public void deleteUser(Long userId) {
        log.info("Deleting a user");
        var userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No possible to find user to delete");
                    return new NoFoundUserException("No found user by id = %s".formatted(userId));
                });
        userRepository.delete(userToDelete);
    }

    public User updateUser(Long userId, User userToUpdate) {
        var previousUserVersion = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No possible to find user to update");
                    return new NoFoundUserException("No found user by id = %s".formatted(userId));
                });
        log.info("Checking lists for previous version user and new user");
        if (!arePetsListsSame(previousUserVersion.pets(), userToUpdate.pets())) {
            log.error("No possible to update pets using /users/{id} endpoint");
            throw new NotAllowedEditException(
                    "Pets cannot be updated via /users endpoint. " +
                            "Use /users/{id}/pets endpoints to manage pets."
            );
        }
        var updatedUser = new User(
                userId,
                userToUpdate.name(),
                userToUpdate.email(),
                userToUpdate.age(),
                new ArrayList<>(previousUserVersion.pets())
        );
        log.info("Deleting a previous user`s version");
        userRepository.delete(previousUserVersion);
        log.info("Saving a new user`s version");
        userRepository.save(updatedUser);
        return updatedUser;

    }

    public List<User> searchAllUsers(String maxAge) {
        log.info("Searching for all users");
        return userRepository.getUsers().stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(User::id))
                .filter(user -> isNull(maxAge) || user.age() <= Integer.parseInt(maxAge))
                .toList();
    }

    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("No possible to find user");
                    return new NoFoundUserException("No found user by id = %s".formatted(userId));
                });
    }

    private boolean isRegistered(String email) {
        if (userRepository.getUsers().stream().
                filter(Objects::nonNull).anyMatch(user -> email.equals(user.email()))) {
            log.error("This user has already registered");
            throw new AlreadyRegisteredException("User with this email is already exists");
        }
        return false;
    }

    private boolean arePetsListsSame(List<Pet> previousPets, List<Pet> incomingPets) {
        if (previousPets.size() != incomingPets.size()) {
            return false;
        }
        for (int i = 0; i < previousPets.size(); i++) {
            Pet oldPet = previousPets.get(i);
            Pet newPet = incomingPets.get(i);

            if (isNull(newPet.id()) || isNull(newPet.userId())) {
                log.error("Pet id and userId are null");
                throw new InvalidPetException("Pet id and userId are necessary");
            }
            if (!Objects.equals(oldPet.id(), newPet.id())) return false;
            if (!Objects.equals(oldPet.name(), newPet.name())) return false;
            if (!Objects.equals(oldPet.userId(), newPet.userId())) return false;
        }
        return true;
    }
}
