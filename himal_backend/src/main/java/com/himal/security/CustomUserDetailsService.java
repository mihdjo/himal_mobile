package com.himal.security;

import com.himal.model.Korisnik;
import com.himal.repository.KorisnikRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/*
    @author: mihdjo
*/

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final KorisnikRepository korisnikRepository;

    public CustomUserDetailsService(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Korisnik nije pronađen."
                ));

        return User
                .withUsername(korisnik.getUsername())
                .password(korisnik.getPasswordHash())
                .authorities(Collections.emptyList())
                .build();
    }
}