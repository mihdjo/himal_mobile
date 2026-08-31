package com.himal.dto;

import java.util.List;

/*
    @author: mihdjo
*/

public class GrupisanaOpremaResponse {

    private Long idEkspedicije;
    private String nazivEkspedicije;
    private Boolean status;
    private List<EkspedicijaOpremaResponse> oprema;

    public GrupisanaOpremaResponse() {
    }

    public GrupisanaOpremaResponse(
            Long idEkspedicije,
            String nazivEkspedicije,
            Boolean status,
            List<EkspedicijaOpremaResponse> oprema
    ) {
        this.idEkspedicije = idEkspedicije;
        this.nazivEkspedicije = nazivEkspedicije;
        this.status = status;
        this.oprema = oprema;
    }

    public Long getIdEkspedicije() {
        return idEkspedicije;
    }

    public void setIdEkspedicije(Long idEkspedicije) {
        this.idEkspedicije = idEkspedicije;
    }

    public String getNazivEkspedicije() {
        return nazivEkspedicije;
    }

    public void setNazivEkspedicije(String nazivEkspedicije) {
        this.nazivEkspedicije = nazivEkspedicije;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<EkspedicijaOpremaResponse> getOprema() {
        return oprema;
    }

    public void setOprema(List<EkspedicijaOpremaResponse> oprema) {
        this.oprema = oprema;
    }
}