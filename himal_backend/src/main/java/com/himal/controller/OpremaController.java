package com.himal.controller;

import com.himal.dto.CreateOpremaRequest;
import com.himal.dto.OpremaResponse;
import com.himal.service.OpremaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class OpremaController {

    private final OpremaService opremaService;

    public OpremaController(OpremaService opremaService) {
        this.opremaService = opremaService;
    }

    @GetMapping
    public ResponseEntity<List<OpremaResponse>> getAll() {

        return ResponseEntity.ok(
                opremaService.getAll()
        );
    }

    @PostMapping
    public ResponseEntity<OpremaResponse> create(
            @Valid @RequestBody CreateOpremaRequest request
    ) {

        OpremaResponse response =
                opremaService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}