package org.joboffer.domain.loginandregister;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final LoginAndRegisterRepositoryImpl repositoryImpl;

    public User findUserByUserName(String userName) {
        return repositoryImpl.findByUsername(userName);
    }

    public User register(User user) {
        return repositoryImpl.save(user);
    }

}
