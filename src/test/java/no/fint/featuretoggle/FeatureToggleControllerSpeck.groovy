package no.fint.featuretoggle

import spock.lang.Specification

class FeatureToggleControllerSpeck extends Specification {

    def "Strip application name should return feature without application name"() {
        given:
        def controller = new FeatureToggleController()

        when:
        def name = controller.stripApplicationName("test-app","test-app.feature")

        then:
        name == "feature"
    }
}
