package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

@AllArgsConstructor
class LoginAndRegisterValidation {

    private final LoginAndRegisterRepository repository;

    boolean checkingIfUserIdIsNull(UserDto userDto) {
        return userDto.getId() == null;
    }

    boolean checkingIfUserWithTheSameIdExist(String userId) {
        return repository.findAll().stream()
                .anyMatch(user -> user.getId().equals(userId));
    }
}
