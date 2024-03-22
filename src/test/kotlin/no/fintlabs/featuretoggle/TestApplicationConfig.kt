package no.fintlabs.featuretoggle

import io.getunleash.FakeUnleash
import io.getunleash.Unleash
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestApplicationConfig {
    @Bean
    fun unleashClient(): Unleash {
        return FakeUnleash()
    }
}
