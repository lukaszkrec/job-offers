package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUser;
import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUserDto;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final LoginAndRegisterRepository repository;
    private final Map<String, User> users = new HashMap<>();


    public List<UserDto> findAllUsers() {
        return users.values().stream()
                .map(UserDtoMapper::mapToUserDto)
                .toList();
    }


    public UserDto findUserByUserName(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
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
        users.put(user.getId(), user);
        return mapToUserDto(user);
    }

    private boolean userIdIsNull(UserDto userDto) {
        return userDto.getId() == null;
    }

    private boolean userWithTheSameIdExist(String userId) {
        return users.keySet().stream()
                .anyMatch(id -> id.equals(userId));
    }

}
