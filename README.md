# Fint Feature Toggle Service

Expose features for a given application on `/api/feature`

## Configuration

The application requires the following configuration properties:

| Property | Description | Example                      |
| --- | --- |------------------------------|
| `fint.feature-toggle.unleash.api` | The URL of the Unleash API server. This is where the feature toggle service will connect to check the status of feature toggles. | `http://localhost:4242/api/` |
| `fint.feature-toggle.unleash.api-key` | The API key for the Unleash server. This is used to authenticate the feature toggle service with the Unleash server. | `123456890`                  |