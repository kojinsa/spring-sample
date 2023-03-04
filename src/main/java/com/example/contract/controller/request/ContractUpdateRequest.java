package com.example.contract.controller.request;

import com.example.contract.domain.contract.ContractState;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
public class ContractUpdateRequest {

    @NotNull
    private Integer term;

    @NotNull
    private Set<Long> warrantIds;

    @NotNull
    private ContractState state;
}