package com.himal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
    @author: mihdjo
*/

@Entity
@Table(name = "sacuvana_ekspedicija")
public class SacuvanaEkspedicija {

    @EmbeddedId
    private SacuvanaEkspedicijaId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idKorisnika")
    @JoinColumn(name = "id_korisnika", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idEkspedicije")
    @JoinColumn(name = "id_ekspedicije", nullable = false)
    private Ekspedicija ekspedicija;

    @Column(
        name = "datum_cuvanja",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime datumCuvanja;

    public SacuvanaEkspedicija() {
    }

    public SacuvanaEkspedicijaId getId() {
        return id;
    }

    public void setId(SacuvanaEkspedicijaId id) {
        this.id = id;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Ekspedicija getEkspedicija() {
        return ekspedicija;
    }

    public void setEkspedicija(Ekspedicija ekspedicija) {
        this.ekspedicija = ekspedicija;
    }

    public LocalDateTime getDatumCuvanja() {
        return datumCuvanja;
    }

    public void setDatumCuvanja(LocalDateTime datumCuvanja) {
        this.datumCuvanja = datumCuvanja;
    }
}