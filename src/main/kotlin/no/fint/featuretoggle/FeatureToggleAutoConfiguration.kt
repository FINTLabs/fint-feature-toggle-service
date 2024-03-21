package no.fint.featuretoggle

import io.getunleash.DefaultUnleash
import io.getunleash.Unleash
import io.getunleash.util.UnleashConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnProperty(prefix = "fint.feature-toggle", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(UnleashProperties::class)
class FeatureToggleAutoConfiguration(
    @Value("\${spring.application.name}") private val applicationName: String,
) {
    @Bean
    @ConditionalOnMissingBean
    fun unleashClient(unleashProperties: UnleashProperties): Unleash {
        val unleashConfig =
            UnleashConfig.builder()
                .appName(applicationName)
                .instanceId(unleashProperties.instanceId)
                .unleashAPI(unleashProperties.api)
                .apiKey(unleashProperties.apiKey)
                .synchronousFetchOnInitialisation(true)
                .build()
        return DefaultUnleash(unleashConfig)
    }

    @Bean
    fun featureToggleController(unleashClient: Unleash): FeatureToggleController {
        return FeatureToggleController(applicationName, unleashClient)
    }
}
