package org.joboffer.domain.loginandregister;

import java.util.Map;

interface LoginAndRegisterRepository {

    User findByUsername(String username);

    User save(User user);

    Map<String, User> findAll();
}
