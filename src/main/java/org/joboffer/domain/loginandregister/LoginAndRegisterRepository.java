package org.joboffer.domain.loginandregister;

import org.joboffer.domain.loginandregister.dto.UserDto;

import java.util.List;

interface LoginAndRegisterRepository {


    User findByUsername(String username);

    User save(User user);

    List<User> findAll();
}
