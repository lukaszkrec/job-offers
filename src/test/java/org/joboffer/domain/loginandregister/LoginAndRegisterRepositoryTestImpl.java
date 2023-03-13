package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUser;
import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUserDto;

@AllArgsConstructor
class LoginAndRegisterRepositoryTestImpl implements LoginAndRegisterRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return users.values().stream()
                .toList();
    }

    @Override
    public User findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UserDoesNotExistException("User with username " + username + " does not exist"));
    }

    @Override
    public User save(User user) {
        if (userIdIsNull(user)) {
            throw new IllegalArgumentException("User must have an id");
        }
        if (userWithTheSameIdExist(user.getId())) {
            throw new UserAlreadyExistException("User with id " + user.getId() + " already exists");
        }
        users.put(user.getId(), user);
        return user;
    }

    private boolean userIdIsNull(User user) {
        return user.getId() == null;
    }

    private boolean userWithTheSameIdExist(String userId) {
        return users.keySet().stream()
                .anyMatch(id -> id.equals(userId));
    }
}
