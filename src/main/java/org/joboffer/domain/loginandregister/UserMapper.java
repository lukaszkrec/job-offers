package org.joboffer.domain.loginandregister;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

    }

    public static User mapToUser(UserDto dto) {
        return User.builder()
                .id(dto.id())
                .username(dto.username())
                .password(dto.password())
                .build();
    }
}
