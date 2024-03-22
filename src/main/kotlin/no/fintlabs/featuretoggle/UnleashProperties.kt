package no.fintlabs.featuretoggle

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("fint.feature-toggle.unleash")
@ConstructorBinding
data class UnleashProperties(
    val api: String,
    val apiKey: String,
)
