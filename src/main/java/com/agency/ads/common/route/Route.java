package com.agency.ads.common.route;

public interface Route {
    String HEALTH = "/health";
    String ALL = "/**";

    // ADS
    String ADS = "/ads"; //BASE -- POST TO CREATE
    String GET_BY_ID_DEALER_AND_STAGE = "/by/{dealer_id}/dealer/{state}/state";
    String PUBLISH = "/publish";
    String UNPUBLISH = "/unpublish";

    // DEALER
    String DEALER = "/dealer"; //BASE -- POST TO CREATE
    String DEALER_ID = "/{dealer_id}";
}
