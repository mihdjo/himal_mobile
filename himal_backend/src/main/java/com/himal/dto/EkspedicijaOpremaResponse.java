package com.himal.dto;

/*
    @author: mihdjo
*/

public class EkspedicijaOpremaResponse {

    private Long idOpreme;
    private String naziv;
    private String opis;

    private Boolean obavezna;
    private Integer kolicina;
    private String napomena;

    public EkspedicijaOpremaResponse() {
    }

    public EkspedicijaOpremaResponse(
            Long idOpreme,
            String naziv,
            String opis,
            Boolean obavezna,
            Integer kolicina,
            String napomena
    ) {
        this.idOpreme = idOpreme;
        this.naziv = naziv;
        this.opis = opis;
        this.obavezna = obavezna;
        this.kolicina = kolicina;
        this.napomena = napomena;
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