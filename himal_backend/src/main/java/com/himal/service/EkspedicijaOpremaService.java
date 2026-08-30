package com.himal.service;

import com.himal.dto.AddEkspedicijaOpremaRequest;
import com.himal.dto.EkspedicijaOpremaResponse;
import com.himal.model.Ekspedicija;
import com.himal.model.EkspedicijaOprema;
import com.himal.model.EkspedicijaOpremaId;
import com.himal.model.Oprema;
import com.himal.repository.EkspedicijaOpremaRepository;
import com.himal.repository.EkspedicijaRepository;
import com.himal.repository.OpremaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.himal.dto.UpdateEkspedicijaOpremaRequest;
import java.util.List;

/*
    @author: mihdjo
*/

@Service
public class EkspedicijaOpremaService {

    private final EkspedicijaOpremaRepository ekspedicijaOpremaRepository;
    private final EkspedicijaRepository ekspedicijaRepository;
    private final OpremaRepository opremaRepository;

    public EkspedicijaOpremaService(
            EkspedicijaOpremaRepository ekspedicijaOpremaRepository,
            EkspedicijaRepository ekspedicijaRepository,
            OpremaRepository opremaRepository
    ) {
        this.ekspedicijaOpremaRepository = ekspedicijaOpremaRepository;
        this.ekspedicijaRepository = ekspedicijaRepository;
        this.opremaRepository = opremaRepository;
    }

    @Transactional(readOnly = true)
    public List<EkspedicijaOpremaResponse> getByExpedition(
            Long idEkspedicije
    ) {

        if (!ekspedicijaRepository.existsById(idEkspedicije)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ekspedicija nije pronađena."
            );
        }

        return ekspedicijaOpremaRepository
                .findByEkspedicija_IdEkspedicije(idEkspedicije)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public EkspedicijaOpremaResponse add(
            Long idEkspedicije,
            String username,
            AddEkspedicijaOpremaRequest request
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
                    "Možete menjati opremu samo sopstvene ekspedicije."
            );
        }

        Oprema oprema = opremaRepository
                .findById(request.getIdOpreme())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Oprema nije pronađena."
                ));

        EkspedicijaOpremaId id =
                new EkspedicijaOpremaId(
                        idEkspedicije,
                        oprema.getIdOpreme()
                );

        if (ekspedicijaOpremaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Oprema je već dodata ovoj ekspediciji."
            );
        }

        EkspedicijaOprema ekspedicijaOprema =
                new EkspedicijaOprema();

        ekspedicijaOprema.setId(id);
        ekspedicijaOprema.setEkspedicija(ekspedicija);
        ekspedicijaOprema.setOprema(oprema);
        ekspedicijaOprema.setObavezna(request.getObavezna());
        ekspedicijaOprema.setKolicina(request.getKolicina());

        if (request.getNapomena() == null
                || request.getNapomena().isBlank()) {

            ekspedicijaOprema.setNapomena(null);

        } else {

            ekspedicijaOprema.setNapomena(
                    request.getNapomena().trim()
            );
        }

        EkspedicijaOprema sacuvana =
                ekspedicijaOpremaRepository.save(
                        ekspedicijaOprema
                );

        return toResponse(sacuvana);
    }

    private EkspedicijaOpremaResponse toResponse(
            EkspedicijaOprema eo
    ) {

        return new EkspedicijaOpremaResponse(
                eo.getOprema().getIdOpreme(),
                eo.getOprema().getNaziv(),
                eo.getOprema().getOpis(),
                eo.getObavezna(),
                eo.getKolicina(),
                eo.getNapomena()
        );
    }

    @Transactional
    public EkspedicijaOpremaResponse update(
            Long idEkspedicije,
            Long idOpreme,
            String username,
            UpdateEkspedicijaOpremaRequest request
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
                    "Možete menjati opremu samo sopstvene ekspedicije."
            );
        }

        EkspedicijaOpremaId id
                = new EkspedicijaOpremaId(
                        idEkspedicije,
                        idOpreme
                );

        EkspedicijaOprema ekspedicijaOprema
                = ekspedicijaOpremaRepository
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Oprema nije pronađena na ovoj ekspediciji."
                ));

        ekspedicijaOprema.setObavezna(
                request.getObavezna()
        );

        ekspedicijaOprema.setKolicina(
                request.getKolicina()
        );

        if (request.getNapomena() == null
                || request.getNapomena().isBlank()) {

            ekspedicijaOprema.setNapomena(null);

        } else {

            ekspedicijaOprema.setNapomena(
                    request.getNapomena().trim()
            );
        }

        EkspedicijaOprema sacuvana
                = ekspedicijaOpremaRepository.save(
                        ekspedicijaOprema
                );

        return toResponse(sacuvana);
    }

    @Transactional
    public void remove(
            Long idEkspedicije,
            Long idOpreme,
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
                    "Možete menjati opremu samo sopstvene ekspedicije."
            );
        }

        EkspedicijaOpremaId id
                = new EkspedicijaOpremaId(
                        idEkspedicije,
                        idOpreme
                );

        if (!ekspedicijaOpremaRepository.existsById(id)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Oprema nije pronađena na ovoj ekspediciji."
            );
        }

        ekspedicijaOpremaRepository.deleteById(id);
    }
}
