package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.dto.RegisterUserDto;
import org.joboffer.domain.loginandregister.dto.RegistrationResultDto;
import org.joboffer.domain.loginandregister.dto.UserDto;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;

import static org.joboffer.domain.loginandregister.UserMapper.mapToUserDto;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final LoginAndRegisterRepository repository;

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        User user = User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();
        repository.save(user);
        return new RegistrationResultDto(user.getId(), true, user.getUsername());
    }

    public UserDto findUserByUserName(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User with username: " + username + " does not exist"));
        return mapToUserDto(user);
    }

    public List<UserDto> findAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }
}
