# Credit Card Upgrade Sample

This project demonstrates a Spring Boot web application that upgrades a customer's credit card by calling mainframe services exposed through IBM z/OS Connect.  The application communicates with the mainframe using REST and secures the interaction with OAuth2 bearer tokens.

## Features

* REST endpoint `/api/cards/upgrade` that receives upgrade requests.
* `WebClient` configured with OAuth2 **client credentials** retrieves a JWT and calls the z/OS Connect API.
* Spring Security OAuth2 resource server protects the endpoint with JWT bearer tokens.
* WireMock based tests simulate the mainframe API and an OAuth token service so the application can be tested locally on Windows with no mainframe.

## Project layout
```
src/main/java/com/example/creditcard
├── CreditCardUpgradeApplication.java
├── config
│   ├── SecurityConfig.java
│   └── WebClientConfig.java
├── controller
│   └── CreditCardUpgradeController.java
├── model
│   ├── CreditCardUpgradeRequest.java
│   └── CreditCardUpgradeResponse.java
└── service
    └── CreditCardUpgradeService.java
```

## Running tests

1. Ensure JDK 17 and Maven are installed.
2. Execute `mvn test`.
   WireMock spins up a mock mainframe and OAuth server, generating tokens and responses for the upgrade request.

The tests verify that the application obtains a token, sends it to the mocked z/OS Connect endpoint and returns the upgrade response.

## Calling the service

Run the application with `mvn spring-boot:run` and issue a request:

```
POST http://localhost:8080/api/cards/upgrade
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "accountNumber": "123456",
  "currentCardType": "SILVER",
  "desiredCardType": "GOLD"
}
```

The app forwards the request to z/OS Connect using a new OAuth2 token and returns the mainframe response.
