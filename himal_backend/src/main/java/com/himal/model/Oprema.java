package com.himal.model;

import jakarta.persistence.*;

/*
    @author: mihdjo
*/

@Entity
@Table(name = "oprema")
public class Oprema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opreme")
    private Long idOpreme;

    @Column(name = "naziv", nullable = false, unique = true, length = 100)
    private String naziv;

    @Column(name = "opis", length = 255)
    private String opis;

    public Oprema() {
    }

    public Long getIdOpreme() {
        return idOpreme;
    }

    public void setIdOpreme(Long idOpreme) {
        this.idOpreme = idOpreme;
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
}