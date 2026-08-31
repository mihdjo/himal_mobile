package com.himal.service;

import com.himal.dto.TipEkspedicijeResponse;
import com.himal.repository.TipEkspedicijeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
    @author: mihdjo
*/

@Service
public class TipEkspedicijeService {

    private final TipEkspedicijeRepository tipEkspedicijeRepository;

    public TipEkspedicijeService(
            TipEkspedicijeRepository tipEkspedicijeRepository
    ) {
        this.tipEkspedicijeRepository = tipEkspedicijeRepository;
    }

    @Transactional(readOnly = true)
    public List<TipEkspedicijeResponse> getAll() {

        return tipEkspedicijeRepository
                .findAll()
                .stream()
                .map(tip -> new TipEkspedicijeResponse(
                        tip.getIdTipEkspedicije(),
                        tip.getTip()
                ))
                .toList();
    }
}