package org.example.springbootwebmvc_user_andpets.controller;

import org.example.springbootwebmvc_user_andpets.model.UserDto;
import org.example.springbootwebmvc_user_andpets.repository.UserRepository;
import org.example.springbootwebmvc_user_andpets.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userRepository.getUsers().clear();
    }

    @Test
    void shouldSuccessGetAllUsers() throws Exception {
        var user1 = new UserDto(
                null,
                "test-user1",
                "test-user1@example.com",
                40,
                new ArrayList<>()
        );
        var user2 = new UserDto(
                null,
                "test-user2",
                "test-user2@example.com",
                40,
                new ArrayList<>()
        );
        user1 = userService.createUser(user1);
        user2 = userService.createUser(user2);

        String responseJson = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var users = objectMapper.readValue(
                responseJson,
                new TypeReference<List<UserDto>>() {
                }
        );

        org.assertj.core.api.Assertions.assertThat(users)
                .extracting(UserDto::id)
                .contains(user1.id(), user2.id());
    }

    @Test
    void shouldSuccessSearchUserById() throws Exception {
        var user = new UserDto(
                null,
                "test-user",
                "test-user@example.com",
                40,
                new ArrayList<>()
        );
        user = userService.createUser(user);

        String foundUserJson = mockMvc.perform(get("/users/{id}", user.id()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserDto foundUser = objectMapper.readValue(foundUserJson, UserDto.class);
        org.assertj.core.api.Assertions.assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);

    }

    @Test
    void shouldReturnNotFoundWhenUserNotPresent() throws Exception {
        mockMvc.perform(get("/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404));
    }

    @Test
    void shouldSuccessCreateUser() throws Exception {
        var user = new UserDto(
                null,
                "test-user",
                "test-user@example.com",
                40,
                new ArrayList<>()
        );
        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(createdUserJson, UserDto.class);
        Assertions.assertEquals(user.name(), userResponse.name());
        Assertions.assertNotNull(userResponse.id());
    }

    @Test
    void shouldNotCreateUserWhenUserNameNotValid() throws Exception {
        var user = new UserDto(
                null,
                "",
                "test-user@example.com",
                40,
                new ArrayList<>()
        );
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                )
                .andExpect(status().is(400));

    }

}