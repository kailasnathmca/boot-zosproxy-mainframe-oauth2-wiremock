package com.example.creditcard;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CreditCardUpgradeIntegrationTest {

    private static final WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());

    static {
        wireMockServer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        String baseUrl = "http://localhost:" + wireMockServer.port();
        registry.add("ZOS_API_BASEURL", () -> baseUrl + "/zos");
        registry.add("OAUTH_TOKEN_URI", () -> baseUrl + "/oauth/token");
        registry.add("OAUTH_JWKS_URI", () -> baseUrl + "/oauth/jwks");
    }

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        wireMockServer.resetAll();
        wireMockServer.stubFor(post(urlEqualTo("/oauth/token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"mainframe-token\",\"token_type\":\"Bearer\",\"expires_in\":3600}")));

        wireMockServer.stubFor(post(urlEqualTo("/zos/card/upgrade"))
                .withHeader("Authorization", equalTo("Bearer mainframe-token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"SUCCESS\",\"message\":\"Upgrade accepted\",\"upgradeId\":\"42\"}")));

        wireMockServer.stubFor(get(urlEqualTo("/oauth/jwks"))
                .willReturn(okJson("{\"keys\":[]}")));
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void upgradeCard() throws Exception {
        String requestBody = "{\"accountNumber\":\"123456\",\"currentCardType\":\"SILVER\",\"desiredCardType\":\"GOLD\"}";

        mockMvc.perform(post("/api/cards/upgrade")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.upgradeId").value("42"));
    }
}
