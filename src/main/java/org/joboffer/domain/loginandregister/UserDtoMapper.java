package org.joboffer.domain.loginandregister;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.joboffer.domain.loginandregister.dto.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDtoMapper {

    static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

    }

    static User mapToUser(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }
}
