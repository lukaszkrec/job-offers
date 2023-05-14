package org.joboffer.infrastructure.security.jwt;


import lombok.AllArgsConstructor;
import org.joboffer.domain.loginandregister.LoginAndRegisterFacade;
import org.joboffer.domain.loginandregister.dto.UserDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@AllArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userDto = loginAndRegisterFacade.findUserByUserName(username);
        return getUser(userDto);
    }

    private User getUser(UserDto user) {
        return new User(
                user.username(),
                user.password(),
                Collections.emptyList());
    }
}
