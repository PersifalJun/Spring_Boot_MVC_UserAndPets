package org.example.springbootwebmvc_user_andpets.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootwebmvc_user_andpets.converter.UserDtoConverter;
import org.example.springbootwebmvc_user_andpets.dto.UserDto;
import org.example.springbootwebmvc_user_andpets.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    public UserController(UserService userService, UserDtoConverter userDtoConverter) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
    }

    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam(value = "maxAge", required = false) String maxAge
    ) {
        log.info("Get request for getAllUsers");
        return userService.searchAllUsers(maxAge)
                .stream()
                .map(userDtoConverter::toUserDto)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(
            @PathVariable("id") Long userId
    ) {
        log.info("Get request for getUserById: id={}", userId);
        var foundUser = userService.getById(userId);
        return userDtoConverter.toUserDto((foundUser));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid UserDto userFromRequest
    ) {
        log.info("Post request for create user: user={}", userFromRequest);
        var createdUser = userService.createUser(
                userDtoConverter.toDomain(userFromRequest));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userDtoConverter.toUserDto(createdUser));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid UserDto userFromRequest
    ) {
        log.info("Put request for update user: id={}, userFromRequest={}",
                id, userFromRequest);
        var updatedUser = userService.updateUser(id, userDtoConverter.toDomain(userFromRequest));
        return userDtoConverter.toUserDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("id") Long id
    ) {
        log.info("Delete request for delete user: id={}", id);
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
