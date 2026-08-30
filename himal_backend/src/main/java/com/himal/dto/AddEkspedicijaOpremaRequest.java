package com.himal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
    @author: mihdjo
*/

public class AddEkspedicijaOpremaRequest {

    @NotNull
    private Long idOpreme;

    @NotNull
    private Boolean obavezna;

    @NotNull
    @Min(1)
    private Integer kolicina;

    @Size(max = 255)
    private String napomena;

    public AddEkspedicijaOpremaRequest() {
    }

    public Long getIdOpreme() {
        return idOpreme;
    }

    public void setIdOpreme(Long idOpreme) {
        this.idOpreme = idOpreme;
    }

    public Boolean getObavezna() {
        return obavezna;
    }

    public void setObavezna(Boolean obavezna) {
        this.obavezna = obavezna;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}