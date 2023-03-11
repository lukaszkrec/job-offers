package org.joboffer.domain.loginandregister;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginAndRegisterFacadeTest {

    private final LoginAndRegisterRepositoryImpl repository = new LoginAndRegisterRepositoryImpl();
    private final LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(repository);

    @Test
    void should_register_when_user_data_is_correct() {
        //given
        User user = new User(1L, "John", "Doe");

        //when
        loginAndRegisterFacade.register(user);

        //then
        List<User> users = repository.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    void should_throw_exception_when_user_register_with_null_id() {
        //given
        User user = new User(null, "John", "Doe");

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> loginAndRegisterFacade.register(user));

    }

    @Test
    void should_throw_exception_when_user_register_with_already_existing_id() {
        //given
        User user1 = new User(1L, "John", "Doe");
        User user2 = new User(1L, "Luke", "Skywalker");

        //when
        loginAndRegisterFacade.register(user1);

        //then
        assertThrows(IllegalArgumentException.class, () -> loginAndRegisterFacade.register(user2));

    }

    @Test
    void should_find_user_by_user_name_when_user_exist() {
        //given
        User user = new User(1L, "John", "Doe");

        //when
        loginAndRegisterFacade.register(user);
        User existingUser = loginAndRegisterFacade.findUserByUserName("John");

        //then
        Assertions.assertThat(existingUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void should_throw_exception_when_user_with_the_given_username_does_not_exist() {
        //given
        String searchUserName = "Luke";
        User user = new User(1L, "John", "Doe");

        //when
        loginAndRegisterFacade.register(user);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> loginAndRegisterFacade.findUserByUserName(searchUserName));

    }
}































