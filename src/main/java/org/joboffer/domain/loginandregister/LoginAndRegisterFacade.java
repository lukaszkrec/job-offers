package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

import java.util.List;

import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUser;
import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUserDto;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final LoginAndRegisterRepository repository;


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
        if (userIdIsNull(userDto)) {
            throw new IllegalArgumentException("User must have an id");
        }
        if (userWithTheSameIdExist(userDto.getId())) {
            throw new UserAlreadyExistException("User with id " + userDto.getId() + " already exists");
        }
        User user = mapToUser(userDto);
        User savedUser = repository.save(user);
        return mapToUserDto(savedUser);
    }

    private boolean userIdIsNull(UserDto userDto) {
        return userDto.getId() == null;
    }

    private boolean userWithTheSameIdExist(String userId) {
        return repository.findAll().stream()
                .anyMatch(user -> user.getId().equals(userId));
    }

}
