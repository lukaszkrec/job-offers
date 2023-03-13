package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

import java.util.List;

import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUser;
import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUserDto;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final LoginAndRegisterRepository repository;
    private final LoginAndRegisterValidation validation;


    public List<UserDto> findAllUsers() {
        return repository.findAll().stream()
                .map(UserDtoMapper::mapToUserDto)
                .toList();
    }


    public UserDto findUserByUserName(String username) {
        return repository.findAll().stream()
                .map(user -> repository.findByUsername(username))
                .map(UserDtoMapper::mapToUserDto)
                .findFirst()
                .orElseThrow(() -> new UserDoesNotExistException("User with username " + username + " does not exist"));
    }


    public UserDto register(UserDto userDto) {
        validation.checkingIfUserIdIsNull(userDto);
        validation.checkingIfUserWithTheSameIdExist(userDto);
        User user = mapToUser(userDto);
        User savedUser = repository.save(user);
        return mapToUserDto(savedUser);
    }


}
