package com.himal.dto;

import com.himal.model.TezinaEkspedicije;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    @author: mihdjo
*/

public class EkspedicijaResponse {

    private Long idEkspedicije;
    private String naziv;
    private String opis;
    private LocalDate datumPolaska;
    private String lokacija;
    private TezinaEkspedicije tezina;
    private Integer trajanjeMin;
    private BigDecimal duzinaKm;
    private String externalUrl;
    private LocalDateTime datumKreiranja;

    private Long idTipEkspedicije;
    private String tipEkspedicije;

    private Long idAutora;
    private String autorUsername;

    public EkspedicijaResponse() {
    }

    public EkspedicijaResponse(
            Long idEkspedicije,
            String naziv,
            String opis,
            LocalDate datumPolaska,
            String lokacija,
            TezinaEkspedicije tezina,
            Integer trajanjeMin,
            BigDecimal duzinaKm,
            String externalUrl,
            LocalDateTime datumKreiranja,
            Long idTipEkspedicije,
            String tipEkspedicije,
            Long idAutora,
            String autorUsername
    ) {
        this.idEkspedicije = idEkspedicije;
        this.naziv = naziv;
        this.opis = opis;
        this.datumPolaska = datumPolaska;
        this.lokacija = lokacija;
        this.tezina = tezina;
        this.trajanjeMin = trajanjeMin;
        this.duzinaKm = duzinaKm;
        this.externalUrl = externalUrl;
        this.datumKreiranja = datumKreiranja;
        this.idTipEkspedicije = idTipEkspedicije;
        this.tipEkspedicije = tipEkspedicije;
        this.idAutora = idAutora;
        this.autorUsername = autorUsername;
    }

    public Long getIdEkspedicije() {
        return idEkspedicije;
    }

    public void setIdEkspedicije(Long idEkspedicije) {
        this.idEkspedicije = idEkspedicije;
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

    public LocalDateTime getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(LocalDateTime datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public Long getIdTipEkspedicije() {
        return idTipEkspedicije;
    }

    public void setIdTipEkspedicije(Long idTipEkspedicije) {
        this.idTipEkspedicije = idTipEkspedicije;
    }

    public String getTipEkspedicije() {
        return tipEkspedicije;
    }

    public void setTipEkspedicije(String tipEkspedicije) {
        this.tipEkspedicije = tipEkspedicije;
    }

    public Long getIdAutora() {
        return idAutora;
    }

    public void setIdAutora(Long idAutora) {
        this.idAutora = idAutora;
    }

    public String getAutorUsername() {
        return autorUsername;
    }

    public void setAutorUsername(String autorUsername) {
        this.autorUsername = autorUsername;
    }
}