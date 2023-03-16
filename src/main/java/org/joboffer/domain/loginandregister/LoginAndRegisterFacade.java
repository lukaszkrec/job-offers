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


    public User register(UserDto userDto) {
        if (validation.checkingIfUserIdIsNull(userDto)) {
            throw new UserValidationException("User id can not be: " + null);
        }
        if (validation.checkingIfUserWithTheSameIdExist(userDto.getId())) {
            throw new UserValidationException("User with id " + userDto.getId() + " already exists");
        }
        User mappedUser = mapToUser(userDto);
        return repository.save(mappedUser);

    }

    public List<UserDto> findAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserDtoMapper::mapToUserDto)
                .toList();
    }


    public UserDto findUserByUserName(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UserValidationException("User with username: " + username + " does not exist"));
        return mapToUserDto(user);
    }

}
