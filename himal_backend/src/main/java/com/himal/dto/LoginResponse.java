package com.himal.dto;

/*
    @author: mihdjo
*/

public class LoginResponse {

    private String token;
    private Long idKorisnika;
    private String username;

    public LoginResponse() {
    }

    public LoginResponse(
            String token,
            Long idKorisnika,
            String username
    ) {
        this.token = token;
        this.idKorisnika = idKorisnika;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(Long idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}