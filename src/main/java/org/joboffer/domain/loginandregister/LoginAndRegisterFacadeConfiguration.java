package org.joboffer.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoginAndRegisterFacadeConfiguration {

    @Bean
    LoginAndRegisterFacade loginAndRegisterFacade(LoginAndRegisterRepository repository) {
        return new LoginAndRegisterFacade(repository);
    }
}
