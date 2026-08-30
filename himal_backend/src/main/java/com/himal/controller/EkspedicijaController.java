package com.himal.controller;

import com.himal.dto.CreateEkspedicijaRequest;
import com.himal.dto.EkspedicijaResponse;
import com.himal.service.EkspedicijaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.himal.dto.UpdateEkspedicijaRequest;
import com.himal.model.TezinaEkspedicije;
import com.himal.service.SacuvanaEkspedicijaService;
import java.math.BigDecimal;

/*
    @author: mihdjo
*/

@RestController
@RequestMapping("/api/expeditions")
public class EkspedicijaController {

    private final EkspedicijaService ekspedicijaService;
    private final SacuvanaEkspedicijaService sacuvanaEkspedicijaService;

    public EkspedicijaController(
            EkspedicijaService ekspedicijaService,
            SacuvanaEkspedicijaService sacuvanaEkspedicijaService
    ) {
        this.ekspedicijaService = ekspedicijaService;
        this.sacuvanaEkspedicijaService = sacuvanaEkspedicijaService;
    }

    @PostMapping
    public ResponseEntity<EkspedicijaResponse> create(
            Authentication authentication,
            @Valid @RequestBody CreateEkspedicijaRequest request
    ) {

        EkspedicijaResponse response =
                ekspedicijaService.create(
                        authentication.getName(),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EkspedicijaResponse> getById(
            @PathVariable Long id
    ) {

        EkspedicijaResponse response
                = ekspedicijaService.getById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EkspedicijaResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) TezinaEkspedicije difficulty,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(required = false) BigDecimal maxDistance
    ) {

        List<EkspedicijaResponse> response
                = ekspedicijaService.getAll(
                        search,
                        location,
                        difficulty,
                        typeId,
                        maxDuration,
                        maxDistance
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EkspedicijaResponse> update(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody UpdateEkspedicijaRequest request
    ) {

        EkspedicijaResponse response
                = ekspedicijaService.update(
                        id,
                        authentication.getName(),
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {

        ekspedicijaService.delete(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<Void> save(
            @PathVariable Long id,
            Authentication authentication
    ) {

        sacuvanaEkspedicijaService.save(
                authentication.getName(),
                id
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}/save")
    public ResponseEntity<Void> removeSaved(
            @PathVariable Long id,
            Authentication authentication
    ) {

        sacuvanaEkspedicijaService.remove(
                authentication.getName(),
                id
        );

        return ResponseEntity.noContent().build();
    }
}
