package com.himal.controller;

import com.himal.dto.TipEkspedicijeResponse;
import com.himal.service.TipEkspedicijeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    @author: mihdjo
*/

@RestController
@RequestMapping("/api/expedition-types")
public class TipEkspedicijeController {

    private final TipEkspedicijeService tipEkspedicijeService;

    public TipEkspedicijeController(
            TipEkspedicijeService tipEkspedicijeService
    ) {
        this.tipEkspedicijeService = tipEkspedicijeService;
    }

    @GetMapping
    public ResponseEntity<List<TipEkspedicijeResponse>> getAll() {

        return ResponseEntity.ok(
                tipEkspedicijeService.getAll()
        );
    }
}