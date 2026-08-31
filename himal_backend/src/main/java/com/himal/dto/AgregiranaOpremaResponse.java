package com.himal.dto;

/*
    @author: mihdjo
*/

public class AgregiranaOpremaResponse {

    private Long idOpreme;
    private String naziv;
    private String opis;
    private Integer ukupnaKolicina;
    private Boolean obavezna;

    public AgregiranaOpremaResponse() {
    }

    public AgregiranaOpremaResponse(
            Long idOpreme,
            String naziv,
            String opis,
            Integer ukupnaKolicina,
            Boolean obavezna
    ) {
        this.idOpreme = idOpreme;
        this.naziv = naziv;
        this.opis = opis;
        this.ukupnaKolicina = ukupnaKolicina;
        this.obavezna = obavezna;
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

    public Integer getUkupnaKolicina() {
        return ukupnaKolicina;
    }

    public void setUkupnaKolicina(Integer ukupnaKolicina) {
        this.ukupnaKolicina = ukupnaKolicina;
    }

    public Boolean getObavezna() {
        return obavezna;
    }

    public void setObavezna(Boolean obavezna) {
        this.obavezna = obavezna;
    }
}