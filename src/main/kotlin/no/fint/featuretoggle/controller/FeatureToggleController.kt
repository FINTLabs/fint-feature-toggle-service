package no.fint.featuretoggle.controller

import io.getunleash.Unleash
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class FeatureToggleController(
    @Value("\${spring.application.name}")
    private val applicationName: String,
    private val unleashClient: Unleash,
) {
    @RequestMapping("/feature")
    fun feature(): ResponseEntity<Map<String, Boolean>> {
        val features =
            unleashClient.more().featureToggleNames
                .filter { it.startsWith(applicationName) }
                .associateBy({ it.replace("$applicationName.", "") }, { unleashClient.isEnabled(it) })
        return ResponseEntity.ok().body(features)
    }
}
