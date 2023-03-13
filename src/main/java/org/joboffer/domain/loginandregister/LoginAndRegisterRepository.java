package org.joboffer.domain.loginandregister;

import org.joboffer.domain.loginandregister.dto.UserDto;

import java.util.List;

interface LoginAndRegisterRepository {


    UserDto findByUsername(String username);

    UserDto save(UserDto userDto);

    List<UserDto> findAll();
}
