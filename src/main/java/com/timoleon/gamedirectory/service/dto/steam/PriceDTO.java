package com.timoleon.gamedirectory.service.dto.steam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceDTO {

    private String currency;
    private Double initial;

    @JsonProperty("final")
    private Double final_price;

    private Double discount_percent;
    private String initial_formatted;
    private String final_formatted;

    public PriceDTO() {}

    public PriceDTO(
        String currency,
        Double initial,
        Double final_price,
        Double discount_percent,
        String initial_formatted,
        String final_formatted
    ) {
        this.currency = currency;
        this.initial = initial;
        this.final_price = final_price;
        this.discount_percent = discount_percent;
        this.initial_formatted = initial_formatted;
        this.final_formatted = final_formatted;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getInitial() {
        return initial;
    }

    public void setInitial(Double initial) {
        this.initial = initial;
    }

    public Double getFinal_price() {
        return final_price;
    }

    public void setFinal_price(Double final_price) {
        this.final_price = final_price;
    }

    public Double getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(Double discount_percent) {
        this.discount_percent = discount_percent;
    }

    public String getInitial_formatted() {
        return initial_formatted;
    }

    public void setInitial_formatted(String initial_formatted) {
        this.initial_formatted = initial_formatted;
    }

    public String getFinal_formatted() {
        return final_formatted;
    }

    public void setFinal_formatted(String final_formatted) {
        this.final_formatted = final_formatted;
    }
}
