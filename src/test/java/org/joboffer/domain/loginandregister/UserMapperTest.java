package org.joboffer.domain.loginandregister;

import org.joboffer.domain.loginandregister.dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserMapperTest {

    @Test
    void should_map_to_user_dto() {
        //given
        User user = new User("1", "John", "Doe");

        //when
        UserDto userDto = UserMapper.mapToUserDto(user);

        //then
        assertAll(
                () -> assertThat(user.getId()).isEqualTo(userDto.id()),
                () -> assertThat(user.getUsername()).isEqualTo(userDto.username()),
                () -> assertThat(user.getPassword()).isEqualTo(userDto.password())
        );
    }

    @Test
    void should_map_to_user() {
        //given
        UserDto userDto = new UserDto("1", "John", "Doe");

        //when
        User user = UserMapper.mapToUser(userDto);

        //then
        assertAll(
                () -> assertThat(user.getId()).isEqualTo(userDto.id()),
                () -> assertThat(user.getUsername()).isEqualTo(userDto.username()),
                () -> assertThat(user.getPassword()).isEqualTo(userDto.password())
        );
    }
}