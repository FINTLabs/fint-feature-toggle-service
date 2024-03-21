package no.fint.featuretoggle.annotation

import no.fint.featuretoggle.config.FintUnleashConfig
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(FintUnleashConfig::class)
annotation class EnableFintFeatureToggle
