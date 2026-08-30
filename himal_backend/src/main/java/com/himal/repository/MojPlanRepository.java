package com.himal.repository;

import com.himal.model.MojPlan;
import com.himal.model.MojPlanId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
    @author: mihdjo
*/

public interface MojPlanRepository
        extends JpaRepository<MojPlan, MojPlanId> {

    List<MojPlan> findByKorisnik_IdKorisnika(Long idKorisnika);
}