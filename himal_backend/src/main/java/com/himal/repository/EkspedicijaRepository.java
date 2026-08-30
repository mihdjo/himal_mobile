package com.himal.repository;

import com.himal.model.Ekspedicija;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
    @author: mihdjo
*/

public interface EkspedicijaRepository
        extends JpaRepository<Ekspedicija, Long> {

    List<Ekspedicija> findByKorisnik_IdKorisnikaOrderByDatumKreiranjaDesc(
            Long idKorisnika
    );
}