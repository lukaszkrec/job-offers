package org.joboffer.domain.loginandregister.dto;

import lombok.*;

@Builder
public record UserDto(String id, String username, String password) {
}
