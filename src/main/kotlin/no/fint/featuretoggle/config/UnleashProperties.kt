package no.fint.featuretoggle.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("fint.unleash")
class UnleashProperties {
    lateinit var instanceId: String
    lateinit var api: String
    lateinit var apiKey: String
}
