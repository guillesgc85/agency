package com.agency.ads.app.controller;

import com.agency.ads.common.enums.AdvertisementState;
import com.agency.ads.common.request.AdvertisementCreateRequest;
import com.agency.ads.common.request.AdvertisementPublishRequest;
import com.agency.ads.common.request.AdvertisementUpdateRequest;
import com.agency.ads.common.response.AdvertisementResponse;
import com.agency.ads.common.response.ErrorResponse;
import com.agency.ads.common.route.Route;
import com.agency.ads.core.service.AdsService;
import com.agency.ads.domain.entity.AdvertisementEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Advertisement Controller
 */
@RestController
@RequestMapping(value = {(Route.ADS)}, produces = {(MediaType.APPLICATION_JSON_VALUE)})
public class AdsController {

    private AdsService adsService;

    @Autowired
    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    /**
     * Allows to create of a new advertisement.
     * @param advertisementRequest
     * @return a new advertisementResponse with state DRAFT by default
     */
    @ApiOperation(
        value = "Create a new advertisement"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 500, message = "Any random internal server error", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Advertisement created", response = AdvertisementResponse.class)
        }
    )
    @PostMapping
    public AdvertisementResponse create(@RequestBody @Valid AdvertisementCreateRequest advertisementRequest){
        return adsService.create(advertisementRequest);
    }

    /**
     * Allow update a advertisement.
     * @param advertisementUpdateRequest
     * @return a advertisementResponse updated
     */
    @ApiOperation(
        value = "Update advertisement"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 500, message = "Any random internal server error", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Advertisement updated", response = AdvertisementResponse.class)
        }
    )
    @PatchMapping
    public AdvertisementResponse update(@RequestBody @Valid AdvertisementUpdateRequest advertisementUpdateRequest){
        return adsService.update(advertisementUpdateRequest);
    }

    /**
     * Allows to get all advertisement by dealerId and state parameters.
     * @param dealerId
     * @param state
     * @return list of AdvertisementEntity
     */
    @ApiOperation(
        value = "Get advertisement list"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 500, message = "Any random internal server error", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "List of advertisement", response = List.class)
        }
    )
    @GetMapping(Route.GET_BY_ID_DEALER_AND_STAGE)
    public List<AdvertisementEntity> getByDealerIdAndState(
            @PathVariable("dealer_id") UUID dealerId,
            @PathVariable(value = "state") AdvertisementState state) {
        return adsService.getAdsByDealerIdAndState(dealerId, state);
    }

    /**
     * Allow to publish an advertisement.
     * @param advertisementPublishRequest
     * @return an advertisementResponse publish
     */
    @ApiOperation(
        value = "Publish a advertisement"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 500, message = "Any random internal server error", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Advertisement published", response = AdvertisementResponse.class)
        }
    )
    @PostMapping(Route.PUBLISH)
    public AdvertisementResponse publish(@RequestBody @Valid AdvertisementPublishRequest advertisementPublishRequest){
        return adsService.publish(advertisementPublishRequest);
    }

    /**
     * Allow to unPublish an advertisement.
     * @param request
     * @return an advertisementResponse unpublished
     */
    @ApiOperation(
        value = "UnPublish a advertisement"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 500, message = "Any random internal server error", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Advertisement unPublish", response = AdvertisementResponse.class)
        }
    )
    @PostMapping(Route.UNPUBLISH)
    public AdvertisementResponse unPublish(@RequestBody @Valid AdvertisementPublishRequest request){
        return adsService.unPublish(request);
    }
}
