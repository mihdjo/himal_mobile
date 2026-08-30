package com.himal.repository;

import com.himal.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
    @author: mihdjo
*/

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    Optional<Korisnik> findByUsername(String username);

    Optional<Korisnik> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}