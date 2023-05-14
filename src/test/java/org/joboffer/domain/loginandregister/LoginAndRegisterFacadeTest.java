package org.joboffer.domain.loginandregister;

import org.joboffer.domain.loginandregister.dto.RegisterUserDto;
import org.joboffer.domain.loginandregister.dto.RegistrationResultDto;
import org.joboffer.domain.loginandregister.dto.UserDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.junit.jupiter.api.Assertions.assertAll;

class LoginAndRegisterFacadeTest {

    private final LoginAndRegisterRepository repository = new LoginAndRegisterRepositoryTestImpl();
    private final LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(repository);

    @Test
    void should_register_when_user_data_is_correct() {
        //given
        RegisterUserDto userDto = new RegisterUserDto("John", "Doe");

        //when
        RegistrationResultDto registeredUser = loginAndRegisterFacade.register(userDto);

        //then
        assertAll(
                () -> assertThat(repository.findAll()).hasSize(1),
                () -> assertThat(registeredUser.created()).isTrue(),
                () -> assertThat(registeredUser.username()).isEqualTo(userDto.username())
        );
    }

    @Test
    void should_find_user_by_username_when_user_exist() {
        //given
        RegisterUserDto userDto = new RegisterUserDto("John", "Doe");

        //when
        loginAndRegisterFacade.register(userDto);
        UserDto existingUser = loginAndRegisterFacade.findUserByUserName("John");

        //then
        assertAll(
                () -> assertThat(existingUser.username()).isEqualTo(userDto.username()),
                () -> assertThat(repository.findAll()).hasSize(1)
        );
    }

    @Test
    void should_throw_exception_when_user_with_the_given_username_does_not_exist() {
        //given
        String searchUserName = "Luke";

        //when
        //then
        assertAll(
                () -> assertThatRuntimeException()
                        .isThrownBy(() -> loginAndRegisterFacade.findUserByUserName(searchUserName))
                        .withMessage("User with username: " + searchUserName + " does not exist"),
                () -> assertThat(repository.findAll()).isEmpty()
        );
    }

    @Test
    void should_find_all_users_which_exist_in_database() {
        //given
        RegisterUserDto userDto1 = new RegisterUserDto("John", "Doe");
        RegisterUserDto userDto2 = new RegisterUserDto("Luke", "Skywalker");
        loginAndRegisterFacade.register(userDto1);
        loginAndRegisterFacade.register(userDto2);

        //when
        List<UserDto> allUsersFromDatabase = loginAndRegisterFacade.findAllUsers();
        UserDto savedUser1 = allUsersFromDatabase.get(0);
        UserDto savedUser2 = allUsersFromDatabase.get(1);

        User user1 = UserMapper.mapToUser(savedUser1);
        User user2 = UserMapper.mapToUser(savedUser2);

        //then
        assertAll(
                () -> assertThat(allUsersFromDatabase).hasSize(2),
                () -> assertThat(repository.findAll()).hasSize(2),
                () -> assertThat(repository.findAll()).containsExactlyInAnyOrder(user1, user2)
        );
    }
}































