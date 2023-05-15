package org.joboffer.domain.loginandregister;

import org.assertj.core.api.Assertions;
import org.joboffer.domain.BaseIntegrationTest;
import org.joboffer.domain.loginandregister.dto.RegisterUserDto;
import org.joboffer.domain.loginandregister.dto.RegistrationResultDto;
import org.joboffer.infrastructure.loginandregister.dto.JwtResponseDto;
import org.joboffer.infrastructure.loginandregister.dto.TokenRequestDto;
import org.joboffer.infrastructure.security.jwt.JwtAuthenticatorFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserLoginAndRegisterIntegrationTest extends BaseIntegrationTest {

    @SpyBean
    JwtAuthenticatorFacade jwtAuthenticatorFacade;

    @Autowired
    LoginAndRegisterRepository loginAndRegisterRepository;

    @AfterEach
    void tearDown() {
        loginAndRegisterRepository.deleteAll();
    }

    @Test
    @DisplayName("User tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)")
    void should_return_UNAUTHORIZED_401_when_user_made_get_for_token_without_registration() throws Exception {
        // given & when
        TokenRequestDto tokenRequestDto = new TokenRequestDto("someUser", "somePassword");

        ResultActions failedLoginRequest = mvc.perform(post("/token")
                .content(objectMapper.writeValueAsString(tokenRequestDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        failedLoginRequest
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                            "status": "401 UNAUTHORIZED",
                            "timestamp": "2023-05-01T17:33:47.877+02:00",
                            "message": "User with username: someUser does not exist",
                            "description":"uri=/token"
                        }
                        """.trim()));
        assertAll(
                () -> Assertions.assertThat(loginAndRegisterRepository.findAll()).isEmpty(),
                () -> Mockito.verify(jwtAuthenticatorFacade, times(1)).authenticateAndGenerateToken(tokenRequestDto)
        );
    }

    @Test
    @DisplayName("User made GET /offers with no jwt token and system returned UNAUTHORIZED(401)")
    void should_return_UNAUTHORIZED_401_when_user_made_get_for_offers_without_jwt_token() throws Exception {
        //given && when
        RegisterUserDto userDto = new RegisterUserDto("someUser", "somePassword");

        ResultActions registerAction = mvc.perform(post("/register")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult registerActionResult = registerAction.andExpect(status().isCreated()).andReturn();
        String registerActionResultJson = registerActionResult.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerActionResultJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull(),
                () -> assertThat(loginAndRegisterRepository.findAll()).hasSize(1)
        );

        ResultActions failedGetOffersRequest = mvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        failedGetOffersRequest
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)")
    void should_return_OK_200_when_user_tried_to_register_with_correct_credentials() throws Exception {
        //given && when
        RegisterUserDto userDto = new RegisterUserDto("someUser", "somePassword");

        ResultActions registerAction = mvc.perform(post("/register")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult registerActionResult = registerAction.andExpect(status().isCreated()).andReturn();
        String registerActionResultJson = registerActionResult.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerActionResultJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull(),
                () -> assertThat(loginAndRegisterRepository.findAll()).hasSize(1)
        );
    }

    @Test
    @DisplayName("User tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC")
    void should_return_OK_200_when_user_tried_to_get_jwt_token_after_registration() throws Exception {
        //given && when
        RegisterUserDto userDto = new RegisterUserDto("someUser", "somePassword");

        ResultActions registerAction = mvc.perform(post("/register")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        MvcResult registerActionResult = registerAction.andExpect(status().isCreated()).andReturn();
        String registerActionResultJson = registerActionResult.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerActionResultJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull(),
                () -> assertThat(loginAndRegisterRepository.findAll()).hasSize(1)
        );

        TokenRequestDto tokenRequestDto = new TokenRequestDto("someUser", "somePassword");

        ResultActions successLoginRequest = mvc.perform(post("/token")
                .content(objectMapper.writeValueAsString(tokenRequestDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        MvcResult mvcResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(json, JwtResponseDto.class);
        String token = jwtResponse.token();
        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo("someUser"),
                () -> assertThat(token).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$")),
                () -> Mockito.verify(jwtAuthenticatorFacade, times(1)).authenticateAndGenerateToken(tokenRequestDto)
        );
    }
}
