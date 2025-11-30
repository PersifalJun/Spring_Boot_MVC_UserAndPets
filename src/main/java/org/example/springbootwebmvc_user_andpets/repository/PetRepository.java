package org.example.springbootwebmvc_user_andpets.repository;

import org.example.springbootwebmvc_user_andpets.model.PetDto;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.Objects.nonNull;


@Repository
public class PetRepository {
    private final Map<Long, List<PetDto>> userAndPetsMap;

    public PetRepository() {
        this.userAndPetsMap = new HashMap<>();
    }

    public void save(Long userId, PetDto pet) {
        userAndPetsMap
                .computeIfAbsent(userId, id -> new ArrayList<>())
                .add(pet);
    }

    public Optional<PetDto> findById(Long petId) {
        return userAndPetsMap.values().stream().
                filter(Objects::nonNull).
                flatMap(Collection::stream).
                filter(petDto -> Objects.equals(petDto.id(), petId)).
                findFirst();
    }

    public void delete(Long ownerId, PetDto petToDelete) {
        List<PetDto> userPetList = userAndPetsMap.get(ownerId);
        if (nonNull(userPetList)) {
            userPetList.remove(petToDelete);
        }
    }
}
