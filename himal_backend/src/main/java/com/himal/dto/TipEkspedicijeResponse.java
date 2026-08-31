package com.himal.dto;

/*
    @author: mihdjo
*/

public class TipEkspedicijeResponse {

    private Long idTipEkspedicije;
    private String tip;

    public TipEkspedicijeResponse() {
    }

    public TipEkspedicijeResponse(
            Long idTipEkspedicije,
            String tip
    ) {
        this.idTipEkspedicije = idTipEkspedicije;
        this.tip = tip;
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