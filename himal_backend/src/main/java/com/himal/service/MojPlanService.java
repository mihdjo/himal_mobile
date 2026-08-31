package com.himal.service;

import com.himal.dto.MojPlanResponse;
import com.himal.model.Ekspedicija;
import com.himal.model.Korisnik;
import com.himal.model.MojPlan;
import com.himal.model.MojPlanId;
import com.himal.repository.EkspedicijaRepository;
import com.himal.repository.KorisnikRepository;
import com.himal.repository.MojPlanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.himal.dto.AgregiranaOpremaResponse;
import com.himal.model.EkspedicijaOprema;
import com.himal.repository.EkspedicijaOpremaRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import com.himal.dto.EkspedicijaOpremaResponse;
import com.himal.dto.GrupisanaOpremaResponse;
import java.util.Comparator;
import com.himal.dto.UpdateMojPlanStatusRequest;

/*
    @author: mihdjo
*/

@Service
public class MojPlanService {

    private final MojPlanRepository mojPlanRepository;
    private final KorisnikRepository korisnikRepository;
    private final EkspedicijaRepository ekspedicijaRepository;
    private final EkspedicijaService ekspedicijaService;
    private final EkspedicijaOpremaRepository ekspedicijaOpremaRepository;

    public MojPlanService(
            MojPlanRepository mojPlanRepository,
            KorisnikRepository korisnikRepository,
            EkspedicijaRepository ekspedicijaRepository,
            EkspedicijaService ekspedicijaService,
            EkspedicijaOpremaRepository ekspedicijaOpremaRepository
    ) {
        this.mojPlanRepository = mojPlanRepository;
        this.korisnikRepository = korisnikRepository;
        this.ekspedicijaRepository = ekspedicijaRepository;
        this.ekspedicijaService = ekspedicijaService;
        this.ekspedicijaOpremaRepository = ekspedicijaOpremaRepository;
    }

    @Transactional
    public void add(
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

        MojPlanId id = new MojPlanId(
                korisnik.getIdKorisnika(),
                ekspedicija.getIdEkspedicije()
        );

        if (mojPlanRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ekspedicija se već nalazi u delu Moj plan."
            );
        }

        MojPlan mojPlan = new MojPlan();

        mojPlan.setId(id);
        mojPlan.setKorisnik(korisnik);
        mojPlan.setEkspedicija(ekspedicija);
        mojPlan.setStatus(false);

        mojPlanRepository.save(mojPlan);
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

        MojPlanId id = new MojPlanId(
                korisnik.getIdKorisnika(),
                idEkspedicije
        );

        if (!mojPlanRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ekspedicija se ne nalazi u delu Moj plan."
            );
        }

        mojPlanRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MojPlanResponse> getPlan(
            String username
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Korisnik nije pronađen."
                ));

        return mojPlanRepository
                .findByKorisnik_IdKorisnikaOrderByDatumDodavanjaDesc(
                        korisnik.getIdKorisnika()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MojPlanResponse toResponse(MojPlan mojPlan) {

        return new MojPlanResponse(
                ekspedicijaService.toResponse(
                        mojPlan.getEkspedicija()
                ),
                mojPlan.getStatus(),
                mojPlan.getDatumDodavanja()
        );
    }

    @Transactional(readOnly = true)
    public List<AgregiranaOpremaResponse> getAggregatedEquipment(
            String username
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Korisnik nije pronađen."
        ));

        List<MojPlan> planStavke = mojPlanRepository
                .findByKorisnik_IdKorisnikaOrderByDatumDodavanjaDesc(
                        korisnik.getIdKorisnika()
                );

        Map<Long, AgregiranaOpremaResponse> agregiranaOprema
                = new LinkedHashMap<>();

        for (MojPlan stavka : planStavke) {

            Long idEkspedicije
                    = stavka.getEkspedicija().getIdEkspedicije();

            List<EkspedicijaOprema> opremaZaEkspediciju
                    = ekspedicijaOpremaRepository
                            .findByEkspedicija_IdEkspedicije(
                                    idEkspedicije
                            );

            for (EkspedicijaOprema eo : opremaZaEkspediciju) {

                Long idOpreme = eo.getOprema().getIdOpreme();

                AgregiranaOpremaResponse postojeca
                        = agregiranaOprema.get(idOpreme);

                if (postojeca == null) {

                    agregiranaOprema.put(
                            idOpreme,
                            new AgregiranaOpremaResponse(
                                    eo.getOprema().getIdOpreme(),
                                    eo.getOprema().getNaziv(),
                                    eo.getOprema().getOpis(),
                                    eo.getKolicina(),
                                    eo.getObavezna()
                            )
                    );

                } else {

                    postojeca.setUkupnaKolicina(
                            postojeca.getUkupnaKolicina()
                            + eo.getKolicina()
                    );

                    postojeca.setObavezna(
                            Boolean.TRUE.equals(
                                    postojeca.getObavezna()
                            ) || Boolean.TRUE.equals(
                            eo.getObavezna()
                    )
                    );
                }
            }
        }

        return agregiranaOprema.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                AgregiranaOpremaResponse::getNaziv,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GrupisanaOpremaResponse> getGroupedEquipment(
            String username
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Korisnik nije pronađen."
        ));

        List<MojPlan> planStavke = mojPlanRepository
                .findByKorisnik_IdKorisnikaOrderByDatumDodavanjaDesc(
                        korisnik.getIdKorisnika()
                );

        return planStavke
                .stream()
                .map(stavka -> {

                    Ekspedicija ekspedicija
                            = stavka.getEkspedicija();

                    List<EkspedicijaOpremaResponse> oprema
                            = ekspedicijaOpremaRepository
                                    .findByEkspedicija_IdEkspedicije(
                                            ekspedicija.getIdEkspedicije()
                                    )
                                    .stream()
                                    .map(this::toEquipmentResponse)
                                    .sorted(
                                            Comparator.comparing(
                                                    EkspedicijaOpremaResponse::getNaziv,
                                                    String.CASE_INSENSITIVE_ORDER
                                            )
                                    )
                                    .toList();

                    return new GrupisanaOpremaResponse(
                            ekspedicija.getIdEkspedicije(),
                            ekspedicija.getNaziv(),
                            stavka.getStatus(),
                            oprema
                    );
                })
                .toList();
    }

    private EkspedicijaOpremaResponse toEquipmentResponse(
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
    public MojPlanResponse updateStatus(
            String username,
            Long idEkspedicije,
            UpdateMojPlanStatusRequest request
    ) {

        Korisnik korisnik = korisnikRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Korisnik nije pronađen."
        ));

        MojPlanId id = new MojPlanId(
                korisnik.getIdKorisnika(),
                idEkspedicije
        );

        MojPlan mojPlan = mojPlanRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Ekspedicija se ne nalazi u delu Moj plan."
        ));

        mojPlan.setStatus(request.getStatus());

        MojPlan sacuvan = mojPlanRepository.save(mojPlan);

        return toResponse(sacuvan);
    }
}
