package com.himal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    @author: mihdjo
*/

@Entity
@Table(name = "ekspedicija")
public class Ekspedicija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ekspedicije")
    private Long idEkspedicije;

    @Column(name = "naziv", nullable = false, length = 120)
    private String naziv;

    @Column(name = "opis", nullable = false, columnDefinition = "TEXT")
    private String opis;

    @Column(name = "datum_polaska", nullable = false)
    private LocalDate datumPolaska;

    @Column(name = "lokacija", nullable = false, length = 150)
    private String lokacija;

    @Enumerated(EnumType.STRING)
    @Column(name = "tezina", nullable = false, length = 20)
    private TezinaEkspedicije tezina;

    @Column(name = "trajanje_min", nullable = false)
    private Integer trajanjeMin;

    @Column(
        name = "duzina_km",
        nullable = false,
        precision = 7,
        scale = 2
    )
    private BigDecimal duzinaKm;

    @Column(name = "external_url", length = 500)
    private String externalUrl;

    @Column(
        name = "datum_kreiranja",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime datumKreiranja;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tip_ekspedicije", nullable = false)
    private TipEkspedicije tipEkspedicije;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_korisnika", nullable = false)
    private Korisnik korisnik;

    public Ekspedicija() {
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

    public TipEkspedicije getTipEkspedicije() {
        return tipEkspedicije;
    }

    public void setTipEkspedicije(TipEkspedicije tipEkspedicije) {
        this.tipEkspedicije = tipEkspedicije;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}