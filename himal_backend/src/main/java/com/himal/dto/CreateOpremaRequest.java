package com.himal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
    @author: mihdjo
*/

public class CreateOpremaRequest {

    @NotBlank
    @Size(max = 100)
    private String naziv;

    @Size(max = 255)
    private String opis;

    public CreateOpremaRequest() {
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