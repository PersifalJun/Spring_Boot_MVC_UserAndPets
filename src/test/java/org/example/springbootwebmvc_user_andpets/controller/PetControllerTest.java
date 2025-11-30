package org.example.springbootwebmvc_user_andpets.controller;

import org.example.springbootwebmvc_user_andpets.model.PetDto;
import org.example.springbootwebmvc_user_andpets.model.UserDto;
import org.example.springbootwebmvc_user_andpets.service.PetService;
import org.example.springbootwebmvc_user_andpets.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PetService petService;
    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreatePet() throws Exception {
        var user = new UserDto(
                null,
                "test-user",
                "test-user@example.com",
                30,
                new ArrayList<>()
        );
        user = userService.createUser(user);

        var pet = new PetDto(
                null,
                "test-pet",
                null
        );

        String petJson = objectMapper.writeValueAsString(pet);

        String createdPetJson = mockMvc.perform(
                        post("/users/{ownerId}/pets", user.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(petJson)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto petResponse = objectMapper.readValue(createdPetJson, PetDto.class);

        Assertions.assertEquals(pet.name(), petResponse.name());
        Assertions.assertNotNull(petResponse.id());
        Assertions.assertEquals(user.id(), petResponse.userId());
    }

    @Test
    void shouldSuccessDeletePet() throws Exception {
        var user = new UserDto(
                null,
                "test-user",
                "test-user@example.com",
                30,
                new ArrayList<>()
        );
        user = userService.createUser(user);

        var pet = new PetDto(
                null,
                "test-pet",
                null
        );
        pet = petService.createPet(user.id(), pet);

        mockMvc.perform(delete("/users/{ownerId}/pets/{id}", user.id(), pet.id()))
                .andExpect(status().isNoContent());

        var updatedUser = userService.getById(user.id());
        org.assertj.core.api.Assertions.assertThat(updatedUser.pets())
                .extracting(PetDto::id)
                .doesNotContain(pet.id());
    }

    @Test
    void shouldReturnNotFoundWhenOwnerNotExistsOnCreatePet() throws Exception {
        Long notExistingOwnerId = Long.MAX_VALUE;

        var pet = new PetDto(
                null,
                "test-pet",
                null
        );

        String petJson = objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/users/{ownerId}/pets", notExistingOwnerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
                )
                .andExpect(status().isNotFound());
    }
}