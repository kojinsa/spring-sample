package com.example.contract.service;

import com.example.contract.doamin.Warrant;
import com.example.contract.repository.WarrantRepository;
import com.example.contract.web.dto.WarrantSaveRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WarrantService {

    private final WarrantRepository warrantRepository;

    @Transactional
    public @NotNull Warrant created(@NotNull WarrantSaveRequest dto) {
        return warrantRepository.save(dto.toEntity());
    }
}
