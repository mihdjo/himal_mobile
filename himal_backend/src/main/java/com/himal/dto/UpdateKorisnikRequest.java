package com.himal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/*
    @author: mihdjo
*/

public class UpdateKorisnikRequest {

    @NotBlank
    @Size(max = 50)
    private String ime;

    @NotBlank
    @Size(max = 50)
    private String prezime;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotNull
    @Past
    private LocalDate datumRodjenja;

    public UpdateKorisnikRequest() {
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }
}