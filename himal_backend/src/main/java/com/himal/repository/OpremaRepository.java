package com.himal.repository;

import com.himal.model.Oprema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
    @author: mihdjo
*/

public interface OpremaRepository extends JpaRepository<Oprema, Long> {

    Optional<Oprema> findByNaziv(String naziv);

    boolean existsByNaziv(String naziv);
}