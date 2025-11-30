package org.example.springbootwebmvc_user_andpets.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootwebmvc_user_andpets.model.UserDto;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam(value = "maxAge", required= false) String maxAge
    ){
        log.info("Get request for getAllUsers");
        return userService.searchAllUsers(maxAge);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(
            @PathVariable("id") Long userId
    ){
        log.info("Get request for getUserById: id={}",userId);
        return userService.getById(userId);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid UserDto userToCreate
    ){
        log.info("Get request for create user: user={}",userToCreate);
        var createdUser = userService.createUser(userToCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }
    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid UserDto userToUpdate
    ){
        log.info("Put request for update user: id={}, userToUpdate={}",
                id, userToUpdate);
        return userService.updateUser(id,userToUpdate);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteById(
            @PathVariable("id") Long id
    ){
        log.info("Delete request for delete user: id={}",id);
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
