package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

@AllArgsConstructor
class LoginAndRegisterValidation {

    private final LoginAndRegisterRepository repository;

    boolean userIdIsNull(UserDto userDto) {
        return userDto.getId() == null;
    }

    boolean userWithTheSameIdExist(String userId) {
        return repository.findAll().stream()
                .anyMatch(user -> user.getId().equals(userId));
    }

    void checkingIfUserIdIsNull(UserDto userDto) {
        if (userIdIsNull(userDto)) {
            throw new IllegalArgumentException("User must have an id");
        }
    }

    void checkingIfUserWithTheSameIdExist(UserDto userDto) {
        if (userWithTheSameIdExist(userDto.getId())) {
            throw new UserAlreadyExistException("User with id " + userDto.getId() + " already exists");
        }
    }
}
