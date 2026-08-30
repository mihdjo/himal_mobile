package com.himal.repository;

import com.himal.model.EkspedicijaOprema;
import com.himal.model.EkspedicijaOpremaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
    @author: mihdjo
*/

public interface EkspedicijaOpremaRepository
        extends JpaRepository<EkspedicijaOprema, EkspedicijaOpremaId> {

    List<EkspedicijaOprema> findByEkspedicija_IdEkspedicije(
            Long idEkspedicije
    );
}