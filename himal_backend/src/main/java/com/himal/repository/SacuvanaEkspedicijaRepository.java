package com.himal.repository;

import com.himal.model.SacuvanaEkspedicija;
import com.himal.model.SacuvanaEkspedicijaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
    @author: mihdjo
*/

public interface SacuvanaEkspedicijaRepository
        extends JpaRepository<SacuvanaEkspedicija, SacuvanaEkspedicijaId> {

    List<SacuvanaEkspedicija>
            findByKorisnik_IdKorisnikaOrderByDatumCuvanjaDesc(
                    Long idKorisnika
            );
}
