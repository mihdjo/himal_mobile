package com.himal.service;

import com.himal.dto.CreateOpremaRequest;
import com.himal.dto.OpremaResponse;
import com.himal.model.Oprema;
import com.himal.repository.OpremaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/*
    @author: mihdjo
*/

@Service
public class OpremaService {

    private final OpremaRepository opremaRepository;

    public OpremaService(OpremaRepository opremaRepository) {
        this.opremaRepository = opremaRepository;
    }

    @Transactional(readOnly = true)
    public List<OpremaResponse> getAll() {

        return opremaRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public OpremaResponse create(CreateOpremaRequest request) {

        String naziv = request.getNaziv().trim();

        if (opremaRepository.existsByNaziv(naziv)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Oprema sa ovim nazivom već postoji."
            );
        }

        Oprema oprema = new Oprema();

        oprema.setNaziv(naziv);

        if (request.getOpis() == null
                || request.getOpis().isBlank()) {

            oprema.setOpis(null);

        } else {

            oprema.setOpis(request.getOpis().trim());
        }

        Oprema sacuvana = opremaRepository.save(oprema);

        return toResponse(sacuvana);
    }

    OpremaResponse toResponse(Oprema oprema) {

        return new OpremaResponse(
                oprema.getIdOpreme(),
                oprema.getNaziv(),
                oprema.getOpis()
        );
    }
}