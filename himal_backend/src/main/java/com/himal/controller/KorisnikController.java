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
import com.himal.service.SacuvanaEkspedicijaService;
import com.himal.dto.MojPlanResponse;
import com.himal.service.MojPlanService;
import com.himal.dto.AgregiranaOpremaResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import com.himal.dto.GrupisanaOpremaResponse;
import com.himal.dto.UpdateMojPlanStatusRequest;

/*
    @author: mihdjo
*/

@RestController
@RequestMapping("/api/users")
public class KorisnikController {

    private final KorisnikService korisnikService;
    private final EkspedicijaService ekspedicijaService;
    private final SacuvanaEkspedicijaService sacuvanaEkspedicijaService;
    private final MojPlanService mojPlanService;

    public KorisnikController(
            KorisnikService korisnikService,
            EkspedicijaService ekspedicijaService,
            SacuvanaEkspedicijaService sacuvanaEkspedicijaService,
            MojPlanService mojPlanService
    ) {
        this.korisnikService = korisnikService;
        this.ekspedicijaService = ekspedicijaService;
        this.sacuvanaEkspedicijaService = sacuvanaEkspedicijaService;
        this.mojPlanService = mojPlanService;
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

    @GetMapping("/me/saved-expeditions")
    public ResponseEntity<List<EkspedicijaResponse>>
            getSavedExpeditions(
                    Authentication authentication
            ) {

        List<EkspedicijaResponse> response
                = sacuvanaEkspedicijaService.getSaved(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/plan")
    public ResponseEntity<List<MojPlanResponse>> getPlan(
            Authentication authentication
    ) {

        List<MojPlanResponse> response
                = mojPlanService.getPlan(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/me/plan/{expeditionId}")
    public ResponseEntity<Void> addToPlan(
            @PathVariable Long expeditionId,
            Authentication authentication
    ) {

        mojPlanService.add(
                authentication.getName(),
                expeditionId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/me/plan/{expeditionId}")
    public ResponseEntity<Void> removeFromPlan(
            @PathVariable Long expeditionId,
            Authentication authentication
    ) {

        mojPlanService.remove(
                authentication.getName(),
                expeditionId
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/plan/equipment")
    public ResponseEntity<List<AgregiranaOpremaResponse>>
            getAggregatedPlanEquipment(
                    Authentication authentication
            ) {

        List<AgregiranaOpremaResponse> response
                = mojPlanService.getAggregatedEquipment(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/plan/equipment/grouped")
    public ResponseEntity<List<GrupisanaOpremaResponse>>
            getGroupedPlanEquipment(
                    Authentication authentication
            ) {

        List<GrupisanaOpremaResponse> response
                = mojPlanService.getGroupedEquipment(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/plan/{expeditionId}/status")
    public ResponseEntity<MojPlanResponse> updatePlanStatus(
            @PathVariable Long expeditionId,
            Authentication authentication,
            @Valid @RequestBody UpdateMojPlanStatusRequest request
    ) {

        MojPlanResponse response
                = mojPlanService.updateStatus(
                        authentication.getName(),
                        expeditionId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}
