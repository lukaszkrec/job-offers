package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUser;
import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUserDto;

@AllArgsConstructor
class LoginAndRegisterRepositoryImpl implements LoginAndRegisterRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public List<UserDto> findAll() {
        return users.values().stream()
                .map(UserDtoMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .map(UserDtoMapper::mapToUserDto)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public UserDto save(UserDto userDto) {
        if (checkingIsUserIdIsNull(userDto)) {
            throw new IllegalArgumentException("User must have an id");
        }
        if (checkingIsUserWithTheSameIdExist(userDto.getId())) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = mapToUser(userDto);
        users.put(user.getId(), user);
        return mapToUserDto(user);
    }

    private boolean checkingIsUserIdIsNull(UserDto userDto) {
        return userDto.getId() == null;
    }

    private boolean checkingIsUserWithTheSameIdExist(String userId) {
        return users.keySet().stream()
                .anyMatch(id -> id.equals(userId));
    }
}
