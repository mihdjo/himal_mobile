package com.himal.repository;

import com.himal.model.TipEkspedicije;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
    @author: mihdjo
*/

public interface TipEkspedicijeRepository
        extends JpaRepository<TipEkspedicije, Long> {

    Optional<TipEkspedicije> findByTip(String tip);

    boolean existsByTip(String tip);
}