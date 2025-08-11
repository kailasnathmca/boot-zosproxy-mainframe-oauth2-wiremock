package com.example.creditcard.service;

import com.example.creditcard.model.CreditCardUpgradeRequest;
import com.example.creditcard.model.CreditCardUpgradeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CreditCardUpgradeService {

    private final WebClient webClient;
    private final String zosBaseUrl;

    public CreditCardUpgradeService(WebClient webClient,
                                    @Value("${zos.api.baseUrl}") String zosBaseUrl) {
        this.webClient = webClient;
        this.zosBaseUrl = zosBaseUrl;
    }

    public CreditCardUpgradeResponse upgradeCard(CreditCardUpgradeRequest request) {
        return webClient.post()
                .uri(zosBaseUrl + "/card/upgrade")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreditCardUpgradeResponse.class)
                .block();
    }
}
