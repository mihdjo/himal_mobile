package com.himal.service;

import com.himal.dto.KorisnikResponse;
import com.himal.model.Korisnik;
import com.himal.repository.KorisnikRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/*
    @author: mihdjo
*/

@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;

    public KorisnikService(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }

    public KorisnikResponse getCurrentUser(String username) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Korisnik nije pronađen."
                ));

        return new KorisnikResponse(
                korisnik.getIdKorisnika(),
                korisnik.getIme(),
                korisnik.getPrezime(),
                korisnik.getEmail(),
                korisnik.getUsername(),
                korisnik.getDatumRodjenja(),
                korisnik.getDatumKreiranja()
        );
    }
}