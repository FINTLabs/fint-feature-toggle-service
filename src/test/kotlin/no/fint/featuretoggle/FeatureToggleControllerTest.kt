package no.fint.featuretoggle

import io.getunleash.MoreOperations
import io.getunleash.Unleash
import io.mockk.every
import io.mockk.mockk
import no.fint.featuretoggle.controller.FeatureToggleController
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class FeatureToggleControllerTest {

    private lateinit var unleashClient: Unleash
    private lateinit var unleashMore: MoreOperations
    private lateinit var featureToggleController: FeatureToggleController

    @BeforeEach
    fun setup() {
        unleashClient = mockk<Unleash>()
        unleashMore = mockk<MoreOperations>()
        every { unleashClient.more() } returns unleashMore
        featureToggleController = FeatureToggleController("testApp", unleashClient)
    }

    @Test
    @DisplayName("Should return feature toggles for the application")
    fun shouldReturnFeatureTogglesForApplication() {
        every { unleashMore.featureToggleNames } returns listOf("testApp.feature1", "testApp.feature2", "otherApp.feature")
        every { unleashClient.isEnabled("testApp.feature1") } returns true
        every { unleashClient.isEnabled("testApp.feature2") } returns false

        val expected = ResponseEntity.ok().body(mapOf("feature1" to true, "feature2" to false))
        val actual = featureToggleController.feature()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should return empty map when no feature toggles for the application")
    fun shouldReturnEmptyMapWhenNoFeatureTogglesForApplication() {
        every { unleashMore.featureToggleNames } returns listOf("otherApp.feature")

        val expected = ResponseEntity.ok().body(emptyMap<String, Boolean>())
        val actual = featureToggleController.feature()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should return empty map when feature toggle list is empty")
    fun shouldReturnEmptyMapWhenFeatureToggleListIsEmpty() {
        every { unleashMore.featureToggleNames } returns emptyList()

        val expected = ResponseEntity.ok().body(emptyMap<String, Boolean>())
        val actual = featureToggleController.feature()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should return feature toggles for the application with mixed enabled status")
    fun shouldReturnFeatureTogglesForApplicationWithMixedEnabledStatus() {
        every { unleashMore.featureToggleNames } returns listOf("testApp.feature1", "testApp.feature2", "testApp.feature3")
        every { unleashClient.isEnabled("testApp.feature1") } returns true
        every { unleashClient.isEnabled("testApp.feature2") } returns false
        every { unleashClient.isEnabled("testApp.feature3") } returns true

        val expected = ResponseEntity.ok().body(mapOf("feature1" to true, "feature2" to false, "feature3" to true))
        val actual = featureToggleController.feature()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should return feature toggles for the application with all enabled")
    fun shouldReturnFeatureTogglesForApplicationWithAllEnabled() {
        every { unleashMore.featureToggleNames } returns listOf("testApp.feature1", "testApp.feature2")
        every { unleashClient.isEnabled("testApp.feature1") } returns true
        every { unleashClient.isEnabled("testApp.feature2") } returns true

        val expected = ResponseEntity.ok().body(mapOf("feature1" to true, "feature2" to true))
        val actual = featureToggleController.feature()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should return feature toggles for the application with all disabled")
    fun shouldReturnFeatureTogglesForApplicationWithAllDisabled() {
        every { unleashMore.featureToggleNames } returns listOf("testApp.feature1", "testApp.feature2")
        every { unleashClient.isEnabled("testApp.feature1") } returns false
        every { unleashClient.isEnabled("testApp.feature2") } returns false

        val expected = ResponseEntity.ok().body(mapOf("feature1" to false, "feature2" to false))
        val actual = featureToggleController.feature()

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Should return feature toggles for the application ignoring other applications")
    fun shouldReturnFeatureTogglesForApplicationIgnoringOtherApplications() {
        every { unleashMore.featureToggleNames } returns listOf("testApp.feature1", "otherApp.feature1")
        every { unleashClient.isEnabled("testApp.feature1") } returns true
        every { unleashClient.isEnabled("otherApp.feature1") } returns true

        val expected = ResponseEntity.ok().body(mapOf("feature1" to true))
        val actual = featureToggleController.feature()

        assertEquals(expected, actual)
    }
}