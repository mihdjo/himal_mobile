package com.himal.controller;

import com.himal.dto.KorisnikResponse;
import com.himal.service.KorisnikService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.himal.dto.UpdateKorisnikRequest;
import jakarta.validation.Valid;
import com.himal.dto.EkspedicijaResponse;
import com.himal.service.EkspedicijaService;

import java.util.List;

/*
    @author: mihdjo
*/

@RestController
@RequestMapping("/api/users")
public class KorisnikController {

    private final KorisnikService korisnikService;
    private final EkspedicijaService ekspedicijaService;

    public KorisnikController(
            KorisnikService korisnikService,
            EkspedicijaService ekspedicijaService
    ) {
        this.korisnikService = korisnikService;
        this.ekspedicijaService = ekspedicijaService;
    }

    @GetMapping("/me")
    public ResponseEntity<KorisnikResponse> getCurrentUser(
            Authentication authentication
    ) {

        KorisnikResponse response
                = korisnikService.getCurrentUser(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<KorisnikResponse> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UpdateKorisnikRequest request
    ) {

        KorisnikResponse response
                = korisnikService.updateCurrentUser(
                        authentication.getName(),
                        request
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/expeditions")
    public ResponseEntity<List<EkspedicijaResponse>> getMyExpeditions(
            Authentication authentication
    ) {

        List<EkspedicijaResponse> response
                = ekspedicijaService.getMyExpeditions(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }
}
