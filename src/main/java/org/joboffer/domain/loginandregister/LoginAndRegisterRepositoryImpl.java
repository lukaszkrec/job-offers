package org.joboffer.domain.loginandregister;

import java.util.*;

class LoginAndRegisterRepositoryImpl implements LoginAndRegisterRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public Map<String, User> findAll() {
        return users;
    }

    @Override
    public User findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public User save(User user) {
        if (checkingIsUserIdIsNotNull(user)) {
            throw new IllegalArgumentException("User must have an id");
        }
        if (isUserExistsWithSameId(user.getId())) {
            throw new IllegalArgumentException("User already exists");
        }
        users.put(user.getId(), user);
        return user;
    }

    private static boolean checkingIsUserIdIsNotNull(User user) {
        return user.getId() == null;
    }

    private boolean isUserExistsWithSameId(String userId) {
        return users.keySet().stream()
                .anyMatch(id -> id.equals(userId));
    }
}
