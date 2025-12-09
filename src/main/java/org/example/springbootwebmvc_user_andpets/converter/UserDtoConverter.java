package org.example.springbootwebmvc_user_andpets.converter;

import org.example.springbootwebmvc_user_andpets.domain.User;
import org.example.springbootwebmvc_user_andpets.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    private final PetDtoConverter petDtoConverter;

    public UserDtoConverter(PetDtoConverter petDtoConverter) {
        this.petDtoConverter = petDtoConverter;
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.id(),
                user.name(),
                user.email(),
                user.age(),
                user.pets().stream().map(petDtoConverter::toPetDto).toList()
        );
    }

    public User toDomain(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.name(),
                userDto.email(),
                userDto.age(),
                userDto.pets().stream().map(petDtoConverter::toDomain).toList()
        );

    }
}
