package com.himal.service;

import com.himal.dto.EkspedicijaResponse;
import com.himal.model.Ekspedicija;
import com.himal.model.Korisnik;
import com.himal.model.SacuvanaEkspedicija;
import com.himal.model.SacuvanaEkspedicijaId;
import com.himal.repository.EkspedicijaRepository;
import com.himal.repository.KorisnikRepository;
import com.himal.repository.SacuvanaEkspedicijaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/*
    @author: mihdjo
*/

@Service
public class SacuvanaEkspedicijaService {

    private final SacuvanaEkspedicijaRepository sacuvanaEkspedicijaRepository;
    private final KorisnikRepository korisnikRepository;
    private final EkspedicijaRepository ekspedicijaRepository;
    private final EkspedicijaService ekspedicijaService;

    public SacuvanaEkspedicijaService(
            SacuvanaEkspedicijaRepository sacuvanaEkspedicijaRepository,
            KorisnikRepository korisnikRepository,
            EkspedicijaRepository ekspedicijaRepository,
            EkspedicijaService ekspedicijaService
    ) {
        this.sacuvanaEkspedicijaRepository = sacuvanaEkspedicijaRepository;
        this.korisnikRepository = korisnikRepository;
        this.ekspedicijaRepository = ekspedicijaRepository;
        this.ekspedicijaService = ekspedicijaService;
    }

    @Transactional
    public void save(
            String username,
            Long idEkspedicije
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Korisnik nije pronađen."
                ));

        Ekspedicija ekspedicija = ekspedicijaRepository
                .findById(idEkspedicije)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ekspedicija nije pronađena."
                ));

        SacuvanaEkspedicijaId id =
                new SacuvanaEkspedicijaId(
                        korisnik.getIdKorisnika(),
                        ekspedicija.getIdEkspedicije()
                );

        if (sacuvanaEkspedicijaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ekspedicija je već sačuvana."
            );
        }

        SacuvanaEkspedicija sacuvana =
                new SacuvanaEkspedicija();

        sacuvana.setId(id);
        sacuvana.setKorisnik(korisnik);
        sacuvana.setEkspedicija(ekspedicija);

        sacuvanaEkspedicijaRepository.save(sacuvana);
    }

    @Transactional
    public void remove(
            String username,
            Long idEkspedicije
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Korisnik nije pronađen."
                ));

        SacuvanaEkspedicijaId id =
                new SacuvanaEkspedicijaId(
                        korisnik.getIdKorisnika(),
                        idEkspedicije
                );

        if (!sacuvanaEkspedicijaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ekspedicija nije među sačuvanim ekspedicijama."
            );
        }

        sacuvanaEkspedicijaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EkspedicijaResponse> getSaved(
            String username
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Korisnik nije pronađen."
                ));

        return sacuvanaEkspedicijaRepository
                .findByKorisnik_IdKorisnikaOrderByDatumCuvanjaDesc(
                        korisnik.getIdKorisnika()
                )
                .stream()
                .map(SacuvanaEkspedicija::getEkspedicija)
                .map(ekspedicijaService::toResponse)
                .toList();
    }
}