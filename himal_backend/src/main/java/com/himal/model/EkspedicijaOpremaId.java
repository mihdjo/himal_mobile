package com.himal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/*
    @author: mihdjo
*/

@Embeddable
public class EkspedicijaOpremaId implements Serializable {

    @Column(name = "id_ekspedicije")
    private Long idEkspedicije;

    @Column(name = "id_opreme")
    private Long idOpreme;

    public EkspedicijaOpremaId() {
    }

    public EkspedicijaOpremaId(Long idEkspedicije, Long idOpreme) {
        this.idEkspedicije = idEkspedicije;
        this.idOpreme = idOpreme;
    }

    public Long getIdEkspedicije() {
        return idEkspedicije;
    }

    public void setIdEkspedicije(Long idEkspedicije) {
        this.idEkspedicije = idEkspedicije;
    }

    public Long getIdOpreme() {
        return idOpreme;
    }

    public void setIdOpreme(Long idOpreme) {
        this.idOpreme = idOpreme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EkspedicijaOpremaId)) {
            return false;
        }

        EkspedicijaOpremaId that = (EkspedicijaOpremaId) o;

        return Objects.equals(idEkspedicije, that.idEkspedicije)
                && Objects.equals(idOpreme, that.idOpreme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEkspedicije, idOpreme);
    }
}