package com.himal.service;

import com.himal.dto.CreateEkspedicijaRequest;
import com.himal.dto.EkspedicijaResponse;
import com.himal.model.Ekspedicija;
import com.himal.model.Korisnik;
import com.himal.model.TipEkspedicije;
import com.himal.repository.EkspedicijaRepository;
import com.himal.repository.KorisnikRepository;
import com.himal.repository.TipEkspedicijeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import org.springframework.data.domain.Sort;
import com.himal.dto.UpdateEkspedicijaRequest;
import com.himal.model.TezinaEkspedicije;
import java.math.BigDecimal;

/*
    @author: mihdjo
*/

@Service
public class EkspedicijaService {

    private final EkspedicijaRepository ekspedicijaRepository;
    private final KorisnikRepository korisnikRepository;
    private final TipEkspedicijeRepository tipEkspedicijeRepository;

    public EkspedicijaService(
            EkspedicijaRepository ekspedicijaRepository,
            KorisnikRepository korisnikRepository,
            TipEkspedicijeRepository tipEkspedicijeRepository
    ) {
        this.ekspedicijaRepository = ekspedicijaRepository;
        this.korisnikRepository = korisnikRepository;
        this.tipEkspedicijeRepository = tipEkspedicijeRepository;
    }

    @Transactional
    public EkspedicijaResponse create(
            String username,
            CreateEkspedicijaRequest request
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Korisnik nije pronađen."
                ));

        TipEkspedicije tipEkspedicije = tipEkspedicijeRepository
                .findById(request.getIdTipEkspedicije())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tip ekspedicije nije pronađen."
                ));

        Ekspedicija ekspedicija = new Ekspedicija();

        ekspedicija.setNaziv(request.getNaziv().trim());
        ekspedicija.setOpis(request.getOpis().trim());
        ekspedicija.setDatumPolaska(request.getDatumPolaska());
        ekspedicija.setLokacija(request.getLokacija().trim());
        ekspedicija.setTezina(request.getTezina());
        ekspedicija.setTrajanjeMin(request.getTrajanjeMin());
        ekspedicija.setDuzinaKm(request.getDuzinaKm());

        if (request.getExternalUrl() == null
                || request.getExternalUrl().isBlank()) {
            ekspedicija.setExternalUrl(null);
        } else {
            ekspedicija.setExternalUrl(
                    request.getExternalUrl().trim()
            );
        }

        ekspedicija.setTipEkspedicije(tipEkspedicije);
        ekspedicija.setKorisnik(korisnik);

        Ekspedicija sacuvana
                = ekspedicijaRepository.saveAndFlush(ekspedicija);

        Ekspedicija ucitana = ekspedicijaRepository
                .findById(sacuvana.getIdEkspedicije())
                .orElseThrow();

        return toResponse(ucitana);
    }

    public EkspedicijaResponse toResponse(Ekspedicija ekspedicija) {

        return new EkspedicijaResponse(
                ekspedicija.getIdEkspedicije(),
                ekspedicija.getNaziv(),
                ekspedicija.getOpis(),
                ekspedicija.getDatumPolaska(),
                ekspedicija.getLokacija(),
                ekspedicija.getTezina(),
                ekspedicija.getTrajanjeMin(),
                ekspedicija.getDuzinaKm(),
                ekspedicija.getExternalUrl(),
                ekspedicija.getDatumKreiranja(),
                ekspedicija.getTipEkspedicije().getIdTipEkspedicije(),
                ekspedicija.getTipEkspedicije().getTip(),
                ekspedicija.getKorisnik().getIdKorisnika(),
                ekspedicija.getKorisnik().getUsername()
        );
    }

    @Transactional(readOnly = true)
    public EkspedicijaResponse getById(Long idEkspedicije) {

        Ekspedicija ekspedicija = ekspedicijaRepository
                .findById(idEkspedicije)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Ekspedicija nije pronađena."
        ));

        return toResponse(ekspedicija);
    }

    @Transactional(readOnly = true)
    public List<EkspedicijaResponse> getAll(
            String search,
            String location,
            TezinaEkspedicije difficulty,
            Long typeId,
            Integer maxDuration,
            BigDecimal maxDistance
    ) {

        if (maxDuration != null && maxDuration <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Maksimalno trajanje mora biti veće od 0."
            );
        }

        if (maxDistance != null
                && maxDistance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Maksimalna dužina mora biti veća od 0."
            );
        }

        String normalizedSearch
                = search == null || search.isBlank()
                ? null
                : search.trim();

        String normalizedLocation
                = location == null || location.isBlank()
                ? null
                : location.trim();

        return ekspedicijaRepository
                .searchAndFilter(
                        normalizedSearch,
                        normalizedLocation,
                        difficulty,
                        typeId,
                        maxDuration,
                        maxDistance
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EkspedicijaResponse> getMyExpeditions(String username) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Korisnik nije pronađen."
        ));

        return ekspedicijaRepository
                .findByKorisnik_IdKorisnikaOrderByDatumKreiranjaDesc(
                        korisnik.getIdKorisnika()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public EkspedicijaResponse update(
            Long idEkspedicije,
            String username,
            UpdateEkspedicijaRequest request
    ) {

        Ekspedicija ekspedicija = ekspedicijaRepository
                .findById(idEkspedicije)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Ekspedicija nije pronađena."
        ));

        if (!ekspedicija.getKorisnik()
                .getUsername()
                .equals(username)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Možete menjati samo sopstvene ekspedicije."
            );
        }

        TipEkspedicije tipEkspedicije = tipEkspedicijeRepository
                .findById(request.getIdTipEkspedicije())
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Tip ekspedicije nije pronađen."
        ));

        ekspedicija.setNaziv(request.getNaziv().trim());
        ekspedicija.setOpis(request.getOpis().trim());
        ekspedicija.setDatumPolaska(request.getDatumPolaska());
        ekspedicija.setLokacija(request.getLokacija().trim());
        ekspedicija.setTezina(request.getTezina());
        ekspedicija.setTrajanjeMin(request.getTrajanjeMin());
        ekspedicija.setDuzinaKm(request.getDuzinaKm());
        ekspedicija.setTipEkspedicije(tipEkspedicije);

        if (request.getExternalUrl() == null
                || request.getExternalUrl().isBlank()) {

            ekspedicija.setExternalUrl(null);

        } else {

            ekspedicija.setExternalUrl(
                    request.getExternalUrl().trim()
            );
        }

        Ekspedicija sacuvana
                = ekspedicijaRepository.saveAndFlush(ekspedicija);

        return toResponse(sacuvana);
    }

    @Transactional
    public void delete(
            Long idEkspedicije,
            String username
    ) {

        Ekspedicija ekspedicija = ekspedicijaRepository
                .findById(idEkspedicije)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Ekspedicija nije pronađena."
        ));

        if (!ekspedicija.getKorisnik()
                .getUsername()
                .equals(username)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Možete brisati samo sopstvene ekspedicije."
            );
        }

        ekspedicijaRepository.delete(ekspedicija);
    }
}
