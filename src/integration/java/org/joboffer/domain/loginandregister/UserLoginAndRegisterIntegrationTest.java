package org.joboffer.domain.loginandregister;

import org.joboffer.domain.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserLoginAndRegisterIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("User tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)")
    void test11() {
    }

    @Test
    @DisplayName("User made GET /offers with no jwt token and system returned UNAUTHORIZED(401)")
    void test12() {
    }

    @Test
    @DisplayName("User made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)")
    void test13() {
    }

    @Test
    @DisplayName("User tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC")
    void test14() {
    }
}
