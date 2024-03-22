package no.fintlabs.featuretoggle

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("fint.feature-toggle.unleash")
class UnleashProperties {
    lateinit var instanceId: String
    lateinit var api: String
    lateinit var apiKey: String
}
