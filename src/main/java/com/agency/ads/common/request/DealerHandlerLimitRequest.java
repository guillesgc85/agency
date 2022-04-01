package com.agency.ads.common.request;

import com.agency.ads.common.enums.HandlerLimit;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DealerHandlerLimitRequest {

    @NotNull
    private HandlerLimit handlerLimit;

    public HandlerLimit getHandlerLimit() {
        return handlerLimit;
    }

    public void setHandlerLimit(HandlerLimit handlerLimit) {
        this.handlerLimit = handlerLimit;
    }
}
