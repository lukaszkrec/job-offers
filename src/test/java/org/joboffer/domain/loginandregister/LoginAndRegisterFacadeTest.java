package org.joboffer.domain.loginandregister;

import org.joboffer.domain.loginandregister.dto.UserDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.joboffer.domain.loginandregister.UserDtoMapper.mapToUserDto;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginAndRegisterFacadeTest {

    private final LoginAndRegisterRepository repository = new LoginAndRegisterRepositoryTestImpl();
    private final LoginAndRegisterValidation validation = new LoginAndRegisterValidation(repository);
    private final LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(repository, validation);

    @Test
    void should_register_when_user_data_is_correct() {
        //given
        User user = new User("1L", "John", "Doe");

        //when
        UserDto userDto = mapToUserDto(user);
        loginAndRegisterFacade.register(userDto);

        //then
        List<UserDto> users = loginAndRegisterFacade.findAllUsers();
        assertThat(users).hasSize(1);
    }

    @Test
    void should_throw_exception_when_user_register_with_null_id() {
        //given
        User user = new User(null, "John", "Doe");

        //when
        UserDto userDto = mapToUserDto(user);

        //then
        assertThatRuntimeException()
                .isThrownBy(() -> loginAndRegisterFacade.register(userDto)).withMessage("User id can not be: " + null);

    }

    @Test
    void should_throw_exception_when_user_register_with_already_existing_id() {
        //given
        User user1 = new User("1L", "John", "Doe");
        User user2 = new User("1L", "Luke", "Skywalker");

        //when
        UserDto userDto1 = mapToUserDto(user1);
        UserDto userDto2 = mapToUserDto(user2);

        //then
        assertThatRuntimeException()
                .isThrownBy(() -> {
                    loginAndRegisterFacade.register(userDto1);
                    loginAndRegisterFacade.register(userDto2);
                }).withMessage("User with id " + userDto2.getId() + " already exists");

    }

    @Test
    void should_find_user_by_username_when_user_exist() {
        //given
        User user = new User("1L", "John", "Doe");

        //when
        UserDto userDto = mapToUserDto(user);
        loginAndRegisterFacade.register(userDto);
        UserDto existingUser = loginAndRegisterFacade.findUserByUserName("John");

        //then
        assertThat(existingUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void should_throw_exception_when_user_with_the_given_username_does_not_exist() {
        //given
        String searchUserName = "Luke";
        User user = new User("1L", "John", "Doe");

        //when
        //then
        assertThrows(RuntimeException.class, () -> loginAndRegisterFacade.findUserByUserName(searchUserName));
    }
}































