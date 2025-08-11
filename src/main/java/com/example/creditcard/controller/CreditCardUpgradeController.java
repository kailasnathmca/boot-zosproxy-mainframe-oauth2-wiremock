package com.example.creditcard.controller;

import com.example.creditcard.model.CreditCardUpgradeRequest;
import com.example.creditcard.model.CreditCardUpgradeResponse;
import com.example.creditcard.service.CreditCardUpgradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class CreditCardUpgradeController {

    private final CreditCardUpgradeService service;

    public CreditCardUpgradeController(CreditCardUpgradeService service) {
        this.service = service;
    }

    @PostMapping("/upgrade")
    public ResponseEntity<CreditCardUpgradeResponse> upgrade(@RequestBody CreditCardUpgradeRequest request) {
        return ResponseEntity.ok(service.upgradeCard(request));
    }
}
