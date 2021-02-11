package no.fint.featuretoggle;

import no.finn.unleash.DefaultUnleash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FeatureToggleController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final DefaultUnleash unleashClient;

    public FeatureToggleController(DefaultUnleash unleashClient) {
        this.unleashClient = unleashClient;
    }

    @GetMapping("feature")
    public ResponseEntity<Map<String, Boolean>> getFeatures() {

        return ResponseEntity.ok(unleashClient
                .getFeatureToggleNames()
                .stream()
                .filter(feature -> feature.startsWith(applicationName))
                .collect(Collectors.toMap(feature -> stripApplicationName(applicationName, feature), unleashClient::isEnabled)));
    }

    private String stripApplicationName(String applicationName, String feature) {
        return feature.replace(applicationName + ".", "");
    }
}
