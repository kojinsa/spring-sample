package com.example.contract.mock;

import com.example.contract.doamin.Product;
import com.example.contract.web.dto.EstimatedPremium;

import java.math.BigDecimal;

public class EstimatedPremiumImpl implements EstimatedPremium {
    private final Product product;

    public EstimatedPremiumImpl(Product product) {
        this.product = product;
    }

    @Override
    public String getProductTitle() {
        return product.getTitle();
    }

    @Override
    public Integer getTerm() {
        return product.getTerm().getRange();
    }

    @Override
    public BigDecimal getPremium() {
        return product.calculatePremium();
    }
}
