package com.himal.dto;

/*
    @author: mihdjo
*/

public class OpremaResponse {

    private Long idOpreme;
    private String naziv;
    private String opis;

    public OpremaResponse() {
    }

    public OpremaResponse(
            Long idOpreme,
            String naziv,
            String opis
    ) {
        this.idOpreme = idOpreme;
        this.naziv = naziv;
        this.opis = opis;
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