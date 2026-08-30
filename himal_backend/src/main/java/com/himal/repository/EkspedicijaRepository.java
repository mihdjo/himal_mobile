package com.himal.repository;

import com.himal.model.Ekspedicija;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.himal.model.TezinaEkspedicije;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

/*
    @author: mihdjo
*/

public interface EkspedicijaRepository
        extends JpaRepository<Ekspedicija, Long> {

    List<Ekspedicija> findByKorisnik_IdKorisnikaOrderByDatumKreiranjaDesc(
            Long idKorisnika
    );

    @Query("""
    SELECT e
    FROM Ekspedicija e
    WHERE
        (
            :search IS NULL
            OR LOWER(e.naziv) LIKE CONCAT('%', LOWER(:search), '%')
            OR LOWER(e.opis) LIKE CONCAT('%', LOWER(:search), '%')
        )
        AND (
            :location IS NULL
            OR LOWER(e.lokacija) LIKE CONCAT('%', LOWER(:location), '%')
        )
        AND (
            :difficulty IS NULL
            OR e.tezina = :difficulty
        )
        AND (
            :typeId IS NULL
            OR e.tipEkspedicije.idTipEkspedicije = :typeId
        )
        AND (
            :maxDuration IS NULL
            OR e.trajanjeMin <= :maxDuration
        )
        AND (
            :maxDistance IS NULL
            OR e.duzinaKm <= :maxDistance
        )
    ORDER BY e.datumKreiranja DESC
    """)
    List<Ekspedicija> searchAndFilter(
            @Param("search") String search,
            @Param("location") String location,
            @Param("difficulty") TezinaEkspedicije difficulty,
            @Param("typeId") Long typeId,
            @Param("maxDuration") Integer maxDuration,
            @Param("maxDistance") BigDecimal maxDistance
    );
}
