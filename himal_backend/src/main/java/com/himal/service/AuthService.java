package com.himal.service;

import com.himal.dto.RegisterRequest;
import com.himal.dto.RegisterResponse;
import com.himal.model.Korisnik;
import com.himal.repository.KorisnikRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.himal.dto.LoginRequest;
import com.himal.dto.LoginResponse;
import com.himal.security.JwtService;

/*
    @author: mihdjo
*/
@Service
public class AuthService {

    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            KorisnikRepository korisnikRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.korisnikRepository = korisnikRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (korisnikRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username je već zauzet."
            );
        }

        if (korisnikRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email je već registrovan."
            );
        }

        Korisnik korisnik = new Korisnik();

        korisnik.setIme(request.getIme().trim());
        korisnik.setPrezime(request.getPrezime().trim());
        korisnik.setEmail(email);
        korisnik.setUsername(username);
        korisnik.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        korisnik.setDatumRodjenja(request.getDatumRodjenja());

        Korisnik sacuvaniKorisnik
                = korisnikRepository.save(korisnik);

        return new RegisterResponse(
                sacuvaniKorisnik.getIdKorisnika(),
                sacuvaniKorisnik.getIme(),
                sacuvaniKorisnik.getPrezime(),
                sacuvaniKorisnik.getEmail(),
                sacuvaniKorisnik.getUsername()
        );
    }

    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername().trim();

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Neispravno korisničko ime ili lozinka."
        ));

        if (!passwordEncoder.matches(
                request.getPassword(),
                korisnik.getPasswordHash()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Neispravno korisničko ime ili lozinka."
            );
        }

        String token = jwtService.generateToken(
                korisnik.getUsername()
        );

        return new LoginResponse(
                token,
                korisnik.getIdKorisnika(),
                korisnik.getUsername()
        );
    }
}
