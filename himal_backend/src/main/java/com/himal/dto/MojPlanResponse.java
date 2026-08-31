package com.himal.dto;

import java.time.LocalDateTime;

/*
    @author: mihdjo
*/

public class MojPlanResponse {

    private EkspedicijaResponse ekspedicija;
    private Boolean status;
    private LocalDateTime datumDodavanja;

    public MojPlanResponse() {
    }

    public MojPlanResponse(
            EkspedicijaResponse ekspedicija,
            Boolean status,
            LocalDateTime datumDodavanja
    ) {
        this.ekspedicija = ekspedicija;
        this.status = status;
        this.datumDodavanja = datumDodavanja;
    }

    public EkspedicijaResponse getEkspedicija() {
        return ekspedicija;
    }

    public void setEkspedicija(EkspedicijaResponse ekspedicija) {
        this.ekspedicija = ekspedicija;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getDatumDodavanja() {
        return datumDodavanja;
    }

    public void setDatumDodavanja(LocalDateTime datumDodavanja) {
        this.datumDodavanja = datumDodavanja;
    }
}