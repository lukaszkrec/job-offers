package org.joboffer.domain.loginandregister;

import java.util.List;
import java.util.Optional;

interface LoginAndRegisterRepository {


    Optional<User> findByUsername(String username);

    User save(User user);

    List<User> findAll();
}
