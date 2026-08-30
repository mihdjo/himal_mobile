package com.himal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    @author: mihdjo
*/

public class KorisnikResponse {

    private Long idKorisnika;
    private String ime;
    private String prezime;
    private String email;
    private String username;
    private LocalDate datumRodjenja;
    private LocalDateTime datumKreiranja;

    public KorisnikResponse() {
    }

    public KorisnikResponse(
            Long idKorisnika,
            String ime,
            String prezime,
            String email,
            String username,
            LocalDate datumRodjenja,
            LocalDateTime datumKreiranja
    ) {
        this.idKorisnika = idKorisnika;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.username = username;
        this.datumRodjenja = datumRodjenja;
        this.datumKreiranja = datumKreiranja;
    }

    public Long getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(Long idKorisnika) {
        this.idKorisnika = idKorisnika;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public LocalDateTime getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(LocalDateTime datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }
}