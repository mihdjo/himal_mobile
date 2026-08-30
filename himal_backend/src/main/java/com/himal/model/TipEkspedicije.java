package com.himal.model;

import jakarta.persistence.*;

/*
    @author: mihdjo
*/

@Entity
@Table(name = "tip_ekspedicije")
public class TipEkspedicije {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tip_ekspedicije")
    private Long idTipEkspedicije;

    @Column(name = "tip", nullable = false, unique = true, length = 50)
    private String tip;

    public TipEkspedicije() {
    }

    public Long getIdTipEkspedicije() {
        return idTipEkspedicije;
    }

    public void setIdTipEkspedicije(Long idTipEkspedicije) {
        this.idTipEkspedicije = idTipEkspedicije;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}