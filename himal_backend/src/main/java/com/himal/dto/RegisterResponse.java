package com.himal.dto;

/*
    @author: mihdjo
*/

public class RegisterResponse {

    private Long idKorisnika;
    private String ime;
    private String prezime;
    private String email;
    private String username;

    public RegisterResponse() {
    }

    public RegisterResponse(
            Long idKorisnika,
            String ime,
            String prezime,
            String email,
            String username
    ) {
        this.idKorisnika = idKorisnika;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.username = username;
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
}