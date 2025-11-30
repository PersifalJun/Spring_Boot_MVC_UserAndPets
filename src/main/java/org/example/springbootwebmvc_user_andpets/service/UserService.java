package org.example.springbootwebmvc_user_andpets.service;

import org.example.springbootwebmvc_user_andpets.exception.InvalidPetException;
import org.example.springbootwebmvc_user_andpets.exception.NoUserException;
import org.example.springbootwebmvc_user_andpets.exception.NotAllowedEditException;
import org.example.springbootwebmvc_user_andpets.exception.RegistryException;
import org.example.springbootwebmvc_user_andpets.model.PetDto;
import org.example.springbootwebmvc_user_andpets.model.UserDto;
import org.example.springbootwebmvc_user_andpets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Service
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

    public UserDto createUser(UserDto userToCreate) {
        UserDto newUser = null;
        if (!isRegistered(userToCreate.email())) {
            var newUserId = ++idCounter;

            newUser = new UserDto(
                    newUserId,
                    userToCreate.name(),
                    userToCreate.email(),
                    userToCreate.age(),
                    new ArrayList<>()
            );
            userRepository.save(newUser);
            addPetsToNewUser(userToCreate, newUserId);
        }
        return newUser;
    }

    private void addPetsToNewUser(UserDto owner, Long userId) {
        for (PetDto petFromRequest : owner.pets()) {
            petService.createPet(userId, petFromRequest);
        }
    }

    public void deleteUser(Long userId) {
        var userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserException("No found user by id = %s".formatted(userId)));
        userRepository.delete(userToDelete);
    }

    public UserDto updateUser(Long userId, UserDto userToUpdate) {
        var previousUserVersion = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserException("No found user by id = %s".formatted(userId)));

        if (!arePetsListsSame(previousUserVersion.pets(), userToUpdate.pets())) {
            throw new NotAllowedEditException(
                    "Pets cannot be updated via /users endpoint. " +
                            "Use /users/{id}/pets endpoints to manage pets."
            );
        }
        var updatedUser = new UserDto(
                userId,
                userToUpdate.name(),
                userToUpdate.email(),
                userToUpdate.age(),
                new ArrayList<>(previousUserVersion.pets())
        );
        userRepository.delete(previousUserVersion);
        userRepository.save(updatedUser);
        return updatedUser;

    }

    public List<UserDto> searchAllUsers(String maxAge) {
        return userRepository.getUsers().stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(UserDto::id))
                .filter(user -> isNull(maxAge) || user.age() <= Integer.parseInt(maxAge))
                .toList();
    }

    public UserDto getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoUserException("No found user by id = %s".formatted(userId)));
    }

    private boolean isRegistered(String email) {
        if (userRepository.getUsers().stream().
                filter(Objects::nonNull).anyMatch(user -> email.equals(user.email()))) {
            throw new RegistryException("User with this email is already exists");
        }
        return false;
    }

    private boolean arePetsListsSame(List<PetDto> previousPets, List<PetDto> incomingPets) {
        if (previousPets.size() != incomingPets.size()) {
            return false;
        }
        for (int i = 0; i < previousPets.size(); i++) {
            PetDto oldPet = previousPets.get(i);
            PetDto newPet = incomingPets.get(i);

            if (isNull(newPet.id()) || isNull(newPet.userId())) {
                throw new InvalidPetException("Pet id and userId are necessary");
            }
            if (!Objects.equals(oldPet.id(), newPet.id())) return false;
            if (!Objects.equals(oldPet.name(), newPet.name())) return false;
            if (!Objects.equals(oldPet.userId(), newPet.userId())) return false;
        }
        return true;
    }
}
