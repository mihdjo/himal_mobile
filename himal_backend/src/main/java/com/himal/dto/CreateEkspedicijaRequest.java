package com.himal.dto;

import com.himal.model.TezinaEkspedicije;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
    @author: mihdjo
*/

public class CreateEkspedicijaRequest {

    @NotBlank
    @Size(max = 120)
    private String naziv;

    @NotBlank
    private String opis;

    @NotNull
    @FutureOrPresent
    private LocalDate datumPolaska;

    @NotBlank
    @Size(max = 150)
    private String lokacija;

    @NotNull
    private TezinaEkspedicije tezina;

    @NotNull
    @Min(1)
    private Integer trajanjeMin;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal duzinaKm;

    @Size(max = 500)
    private String externalUrl;

    @NotNull
    private Long idTipEkspedicije;

    public CreateEkspedicijaRequest() {
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDate getDatumPolaska() {
        return datumPolaska;
    }

    public void setDatumPolaska(LocalDate datumPolaska) {
        this.datumPolaska = datumPolaska;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public TezinaEkspedicije getTezina() {
        return tezina;
    }

    public void setTezina(TezinaEkspedicije tezina) {
        this.tezina = tezina;
    }

    public Integer getTrajanjeMin() {
        return trajanjeMin;
    }

    public void setTrajanjeMin(Integer trajanjeMin) {
        this.trajanjeMin = trajanjeMin;
    }

    public BigDecimal getDuzinaKm() {
        return duzinaKm;
    }

    public void setDuzinaKm(BigDecimal duzinaKm) {
        this.duzinaKm = duzinaKm;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Long getIdTipEkspedicije() {
        return idTipEkspedicije;
    }

    public void setIdTipEkspedicije(Long idTipEkspedicije) {
        this.idTipEkspedicije = idTipEkspedicije;
    }
}