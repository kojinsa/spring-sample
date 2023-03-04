package com.example.contract.dto.mapper;

import com.example.contract.domain.contract.ContractState;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public interface ContractDetail {

    Long getId();

    Integer getTerm();

    Date getStartDate();

    Date getEndDate();

    BigDecimal getPremium();

    ContractState getState();

    @Value("#{target.product}")
    ProductInfo getProduct();

    @Value("#{target.warrants}")
    Set<WarrantInfo> getWarrants();
}