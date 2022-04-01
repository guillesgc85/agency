package com.agency.ads.app.controller;

import com.agency.ads.common.request.DealerHandlerLimitRequest;
import com.agency.ads.common.request.DealerRequest;
import com.agency.ads.common.response.DealerResponse;
import com.agency.ads.common.response.ErrorResponse;
import com.agency.ads.common.route.Route;
import com.agency.ads.core.service.DealerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.UUID;

/**
 * DealerController
 */
@RestController
@RequestMapping(value = {(Route.DEALER)}, produces = {(MediaType.APPLICATION_JSON_VALUE)})
public class DealerController {

    private DealerService dealerService;

    /**
     * DealerService constructor
     * @param dealerService
     */
    @Autowired
    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    /**
     * Allow create a new Dealer
     * @param dealerRequest
     * @return
     */
    @ApiOperation(
        value = "Allows to create of a new distributor"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 500, message = "Any random internal server error", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Create a dealer", response = DealerResponse.class)
        }
    )
    @PostMapping
    public DealerResponse create(@RequestBody @Valid DealerRequest dealerRequest){
        return dealerService.create(dealerRequest);
    }

    /**
     * Allows changing the way how to handle Tier Limit @see HandlerLimit
     * @param dealerId
     * @param dealerHandlerLimitRequest
     * @return DealerResponse
     */
    @ApiOperation(
        value = "Allows changing the way how to handle Tier Limit "
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 500, message = "Any random internal server error", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Create a dealer", response = DealerResponse.class)
        }
    )
    @PutMapping(Route.DEALER_ID)
    public DealerResponse changeHandlerTierLimit(
            @PathVariable("dealer_id") UUID dealerId,
            @RequestBody @Valid DealerHandlerLimitRequest dealerHandlerLimitRequest){
        return dealerService.updateHandlerLimit(dealerId, dealerHandlerLimitRequest);
    }
}

