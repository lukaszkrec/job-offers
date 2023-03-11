package org.joboffer.domain.loginandregister;

import java.util.ArrayList;
import java.util.List;

class LoginAndRegisterRepositoryImpl implements LoginAndRegisterRepository {

    private List<User> users = new ArrayList<>();

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User findByUsername(String username) {
        return users.stream()
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
        users.add(user);
        return user;
    }

    private static boolean checkingIsUserIdIsNotNull(User user) {
        return user.getId() == null;
    }

    private boolean isUserExistsWithSameId(Long userId) {
        return users.stream()
                .anyMatch(user -> user.getId().equals(userId));
    }
}
