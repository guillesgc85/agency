package com.agency.ads.core.mapper;

import com.agency.ads.common.enums.AdvertisementState;
import com.agency.ads.common.request.AdvertisementCreateRequest;
import com.agency.ads.common.request.AdvertisementUpdateRequest;
import com.agency.ads.common.response.AdvertisementResponse;
import com.agency.ads.domain.entity.AdvertisementEntity;

import java.time.LocalDateTime;

public class AdvertisementMapper {

    public static AdvertisementResponse entityToResponse(AdvertisementEntity entity){
        AdvertisementResponse response = new AdvertisementResponse();
        response.setId(entity.getId());
        response.setDealerId(entity.getDealerId());
        response.setPrice(entity.getPrice());
        response.setVehicle(entity.getVehicle());
        response.setState(entity.getState());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdateAt(entity.getUpdateAt());
        return response;
    }

    public static AdvertisementEntity requestToCreateEntity(AdvertisementCreateRequest request){
        AdvertisementEntity entity = new AdvertisementEntity();
        entity.setDealerId(request.getDealerId());
        entity.setPrice(request.getPrice());
        entity.setState(AdvertisementState.DRAFT);
        entity.setVehicle(request.getVehicle());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public static AdvertisementEntity requestToUpdateEntity(AdvertisementUpdateRequest request, AdvertisementEntity advertisementFound){
        advertisementFound.setPrice(request.getPrice());
        advertisementFound.setVehicle(request.getVehicle());
        advertisementFound.setUpdateAt(LocalDateTime.now());
        return advertisementFound;
    }

    public static AdvertisementEntity publishToUpdateEntity(AdvertisementEntity advertisementFound, AdvertisementState state){
        advertisementFound.setState(state);
        advertisementFound.setUpdateAt(LocalDateTime.now());
        return advertisementFound;
    }


}
