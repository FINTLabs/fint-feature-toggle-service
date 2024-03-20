package no.fint.featuretoggle.featuretoggle

import io.getunleash.FakeUnleash
import io.getunleash.Unleash
import no.fint.featuretoggle.TestApplication
import no.fint.featuretoggle.controller.FeatureToggleController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TestApplication::class, FeatureToggleController::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FeatureToggleControllerIntegrationTest {
    @Autowired
    private lateinit var unleashClient: Unleash
    @Autowired
    private lateinit var featureToggleController: FeatureToggleController

    @Test
    fun `should return empty map when feature toggle list is empty`() {
        // Given
        val fakeUnleash = unleashClient as FakeUnleash
        fakeUnleash.disableAll()

        // When
        val response = featureToggleController.feature()

        // Then
        assert(response.statusCode.is2xxSuccessful)
        assert(response.body?.isEmpty() == true)
    }

    @Test
    fun `should return feature toggles for the application`() {
        // Given
        val fakeUnleash = unleashClient as FakeUnleash
        fakeUnleash.enable("fint-feature-toggle-test.feature1")
        fakeUnleash.disable("fint-feature-toggle-test.feature2")

        // When
        val response = featureToggleController.feature()

        // Then
        assert(response.statusCode.is2xxSuccessful)
        assert(response.body?.containsKey("feature1") == true)
        assert(response.body?.containsKey("feature2") == true)
        assert(response.body?.get("feature1") == true)
        assert(response.body?.get("feature2") == false)
    }

    @Test
    fun `should return empty map when no feature toggles for the application`() {
        // Given
        val fakeUnleash = unleashClient as FakeUnleash
        fakeUnleash.disable("otherApp.feature")

        // When
        val response = featureToggleController.feature()

        // Then
        assert(response.statusCode.is2xxSuccessful)
        assert(response.body?.isEmpty() == true)
    }

    @Test
    fun `should return only feature toggles for the application`() {
        // Given
        val fakeUnleash = unleashClient as FakeUnleash
        fakeUnleash.enable("fint-feature-toggle-test.feature1")
        fakeUnleash.disable(
                "otherApp.feature2",
                "fint-feature-toggle-test.feature3")

        // When
        val response = featureToggleController.feature()

        // Then
        assert(response.statusCode.is2xxSuccessful)
        assert(response.body?.containsKey("feature1") == true)
        assert(response.body?.containsKey("feature2") == false)
        assert(response.body?.containsKey("feature3") == true)
        assert(response.body?.get("feature1") == true)
        assert(response.body?.get("feature3") == false)
    }
}
