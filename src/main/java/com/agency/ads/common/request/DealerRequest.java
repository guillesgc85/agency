package com.agency.ads.common.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DealerRequest {
    private String name;
    private Integer adsLimit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAdsLimit() {
        return adsLimit;
    }

    public void setAdsLimit(Integer adsLimit) {
        this.adsLimit = adsLimit;
    }
}
