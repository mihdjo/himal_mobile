package com.himal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
    @author: mihdjo
*/

@Entity
@Table(name = "moj_plan")
public class MojPlan {

    @EmbeddedId
    private MojPlanId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idKorisnika")
    @JoinColumn(name = "id_korisnika", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idEkspedicije")
    @JoinColumn(name = "id_ekspedicije", nullable = false)
    private Ekspedicija ekspedicija;

    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @Column(
        name = "datum_dodavanja",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime datumDodavanja;

    public MojPlan() {
    }

    public MojPlanId getId() {
        return id;
    }

    public void setId(MojPlanId id) {
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