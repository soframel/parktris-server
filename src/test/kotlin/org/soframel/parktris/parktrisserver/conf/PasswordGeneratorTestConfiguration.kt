package org.soframel.parktris.parktrisserver.conf

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.mockito.Mockito
import org.soframel.parktris.parktrisserver.PasswordGenerator
import org.soframel.parktris.parktrisserver.RandomPasswordGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary



@Profile("test")
@Configuration
class PasswordGeneratorTestConfiguration {
    @Bean
    @Primary
    fun passwordGenerator(): PasswordGenerator {
        return StaticPasswordGenerator()
    }
}