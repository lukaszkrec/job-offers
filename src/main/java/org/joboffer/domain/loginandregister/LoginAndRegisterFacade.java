package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

import java.util.List;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final LoginAndRegisterRepositoryImpl repositoryImpl;

    public UserDto findUserByUserName(String userName) {
        return repositoryImpl.findByUsername(userName);
    }

    public UserDto register(UserDto userDto) {
        return repositoryImpl.save(userDto);
    }

    public List<UserDto> findAllUsers() {
        return repositoryImpl.findAll();
    }

}
