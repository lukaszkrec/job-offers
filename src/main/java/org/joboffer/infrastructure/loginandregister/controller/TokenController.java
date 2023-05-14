package org.joboffer.infrastructure.loginandregister.controller;

import lombok.AllArgsConstructor;
import org.joboffer.infrastructure.loginandregister.dto.JwtResponseDto;
import org.joboffer.infrastructure.loginandregister.dto.TokenRequestDto;
import org.joboffer.infrastructure.security.jwt.JwtAuthenticatorFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class TokenController {

    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@Valid @RequestBody TokenRequestDto tokenRequest) {
        final JwtResponseDto jwtResponse = jwtAuthenticatorFacade.authenticateAndGenerateToken(tokenRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
