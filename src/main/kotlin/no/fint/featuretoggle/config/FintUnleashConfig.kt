package no.fint.featuretoggle.config

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.util.UnleashConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(basePackageClasses = [UnleashProperties::class])
@Configuration
class FintUnleashConfig (
        @Value("\${spring.application.name}")
        private val applicationName: String,
        private val unleashProperties: UnleashProperties
) {
    @Bean
    fun unleashConfig(): UnleashConfig {
        return UnleashConfig.builder()
                .appName(applicationName)
                .instanceId(unleashProperties.instanceId)
                .unleashAPI(unleashProperties.api)
                .apiKey(unleashProperties.apiKey)
                .synchronousFetchOnInitialisation(true)
                .build()
    }

    @Bean
    fun unleashClient(unleashConfig: UnleashConfig): Unleash {
        return DefaultUnleash(unleashConfig)
    }
}