package com.himal.controller;

import com.himal.dto.KorisnikResponse;
import com.himal.service.KorisnikService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

        KorisnikResponse response =
                korisnikService.getCurrentUser(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }
}