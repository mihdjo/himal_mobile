package com.himal.model;

import jakarta.persistence.*;

/*
    @author: mihdjo
*/

@Entity
@Table(name = "ekspedicija_oprema")
public class EkspedicijaOprema {

    @EmbeddedId
    private EkspedicijaOpremaId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idEkspedicije")
    @JoinColumn(name = "id_ekspedicije", nullable = false)
    private Ekspedicija ekspedicija;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idOpreme")
    @JoinColumn(name = "id_opreme", nullable = false)
    private Oprema oprema;

    @Column(name = "obavezna", nullable = false)
    private Boolean obavezna = true;

    @Column(name = "kolicina", nullable = false)
    private Integer kolicina = 1;

    @Column(name = "napomena", length = 255)
    private String napomena;

    public EkspedicijaOprema() {
    }

    public EkspedicijaOpremaId getId() {
        return id;
    }

    public void setId(EkspedicijaOpremaId id) {
        this.id = id;
    }

    public Ekspedicija getEkspedicija() {
        return ekspedicija;
    }

    public void setEkspedicija(Ekspedicija ekspedicija) {
        this.ekspedicija = ekspedicija;
    }

    public Oprema getOprema() {
        return oprema;
    }

    public void setOprema(Oprema oprema) {
        this.oprema = oprema;
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