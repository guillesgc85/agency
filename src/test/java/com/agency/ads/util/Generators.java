package com.agency.ads.util;

import com.agency.ads.common.enums.AdvertisementState;
import com.agency.ads.common.enums.HandlerLimit;
import com.agency.ads.common.request.AdvertisementCreateRequest;
import com.agency.ads.common.request.AdvertisementUpdateRequest;
import com.agency.ads.domain.entity.AdvertisementEntity;
import com.agency.ads.domain.entity.DealerEntity;

import java.time.LocalDateTime;
import java.util.*;

public final class Generators {

    public static final UUID DEALER_ID = UUID.fromString("6139e535-8346-4713-af09-176135b54cc4");

    public static AdvertisementEntity getAdvertisementEntityResponse(UUID dealerId) {

        AdvertisementEntity entity = new AdvertisementEntity();
        entity.setDealerId(dealerId);
        entity.setVehicle("Vehicle");
        Integer price = new Random().nextInt(100000);
        entity.setPrice(price.toString());
        entity.setState(AdvertisementState.DRAFT);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public static DealerEntity getDealerEntityResponse() {

        DealerEntity dealerEntity = new DealerEntity();
        dealerEntity.setId(UUID.randomUUID());
        dealerEntity.setName("DEALER");
        dealerEntity.setTierLimit(5);
        dealerEntity.setHandlerLimit(HandlerLimit.ERROR_MESSAGE);
        return dealerEntity;
    }

    public static AdvertisementCreateRequest getAdvertisementCreateRequest() {

        AdvertisementCreateRequest request = new AdvertisementCreateRequest();
        request.setDealerId(DEALER_ID);
        request.setVehicle("Vehicle");
        Integer price = new Random().nextInt(100000);
        request.setPrice(price.toString());
        return request;
    }

    public static AdvertisementUpdateRequest getAdvertisementUpdateRequest() {

        AdvertisementUpdateRequest request = new AdvertisementUpdateRequest();
        request.setId(UUID.randomUUID());
        request.setDealerId(getDealerEntityResponse().getId());
        request.setVehicle("Vehicle :" + request.getId());
        Integer price = new Random().nextInt(100000);
        request.setPrice(price.toString());
        return request;
    }

    public static List<AdvertisementEntity> getAdvertisementEntityList() {
        return new ArrayList<>(Arrays.asList(
                getAdvertisementEntityResponse(UUID.randomUUID()),
                getAdvertisementEntityResponse(UUID.randomUUID()),
                getAdvertisementEntityResponse(UUID.randomUUID()),
                getAdvertisementEntityResponse(UUID.randomUUID()),
                getAdvertisementEntityResponse(UUID.randomUUID())
        ));
    }
}
