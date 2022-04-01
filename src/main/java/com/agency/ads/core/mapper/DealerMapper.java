package com.agency.ads.core.mapper;

import com.agency.ads.common.enums.HandlerLimit;
import com.agency.ads.common.request.DealerRequest;
import com.agency.ads.common.response.DealerResponse;
import com.agency.ads.domain.entity.DealerEntity;

import java.time.LocalDateTime;

public class DealerMapper {

    public static DealerResponse entityToResponse(DealerEntity entity){
        DealerResponse response = new DealerResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setTierLimit(entity.getTierLimit());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    public static DealerEntity requestToCreateEntity(DealerRequest request){
        DealerEntity entity = new DealerEntity();
        entity.setName(request.getName());
        entity.setTierLimit(request.getAdsLimit());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setHandlerLimit(HandlerLimit.ERROR_MESSAGE);
        return entity;
    }

    public static DealerEntity setHandlerLimit(DealerEntity entity, HandlerLimit handlerLimit){
        entity.setHandlerLimit(handlerLimit);
        entity.setUpdateAt(LocalDateTime.now());
        return entity;
    }
}
