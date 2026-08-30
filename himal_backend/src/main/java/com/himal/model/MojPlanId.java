package com.himal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/*
    @author: mihdjo
*/

@Embeddable
public class MojPlanId implements Serializable {

    @Column(name = "id_korisnika")
    private Long idKorisnika;

    @Column(name = "id_ekspedicije")
    private Long idEkspedicije;

    public MojPlanId() {
    }

    public MojPlanId(Long idKorisnika, Long idEkspedicije) {
        this.idKorisnika = idKorisnika;
        this.idEkspedicije = idEkspedicije;
    }

    public Long getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(Long idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public Long getIdEkspedicije() {
        return idEkspedicije;
    }

    public void setIdEkspedicije(Long idEkspedicije) {
        this.idEkspedicije = idEkspedicije;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MojPlanId)) {
            return false;
        }

        MojPlanId that = (MojPlanId) o;

        return Objects.equals(idKorisnika, that.idKorisnika)
                && Objects.equals(idEkspedicije, that.idEkspedicije);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKorisnika, idEkspedicije);
    }
}