package com.himal.service;

import com.himal.dto.KorisnikResponse;
import com.himal.model.Korisnik;
import com.himal.repository.KorisnikRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.himal.dto.UpdateKorisnikRequest;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public KorisnikResponse updateCurrentUser(
            String username,
            UpdateKorisnikRequest request
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Korisnik nije pronađen."
        ));

        String noviEmail = request.getEmail()
                .trim()
                .toLowerCase();

        if (!korisnik.getEmail().equalsIgnoreCase(noviEmail)
                && korisnikRepository.existsByEmail(noviEmail)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email je već registrovan."
            );
        }

        korisnik.setIme(request.getIme().trim());
        korisnik.setPrezime(request.getPrezime().trim());
        korisnik.setEmail(noviEmail);
        korisnik.setDatumRodjenja(request.getDatumRodjenja());

        Korisnik sacuvaniKorisnik
                = korisnikRepository.save(korisnik);

        return new KorisnikResponse(
                sacuvaniKorisnik.getIdKorisnika(),
                sacuvaniKorisnik.getIme(),
                sacuvaniKorisnik.getPrezime(),
                sacuvaniKorisnik.getEmail(),
                sacuvaniKorisnik.getUsername(),
                sacuvaniKorisnik.getDatumRodjenja(),
                sacuvaniKorisnik.getDatumKreiranja()
        );
    }
}
