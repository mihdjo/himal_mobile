package com.himal.controller;

import com.himal.dto.KorisnikResponse;
import com.himal.service.KorisnikService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.himal.dto.UpdateKorisnikRequest;
import jakarta.validation.Valid;

/*
    @author: mihdjo
*/

@RestController
@RequestMapping("/api/users")
public class KorisnikController {

    private final KorisnikService korisnikService;

    public KorisnikController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
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
}
