package no.fint.featuretoggle

import io.getunleash.FakeUnleash
import io.getunleash.Unleash
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestApplicationConfig {
    @Bean
    fun unleashClient(): Unleash {
        return FakeUnleash()
    }
}
