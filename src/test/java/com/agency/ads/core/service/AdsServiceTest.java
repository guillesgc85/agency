package com.agency.ads.core.service;

import com.agency.ads.app.Application;
import com.agency.ads.common.constant.ErrorCode;
import com.agency.ads.common.enums.AdvertisementState;
import com.agency.ads.common.enums.HandlerLimit;
import com.agency.ads.common.exception.AdsException;
import com.agency.ads.common.request.AdvertisementCreateRequest;
import com.agency.ads.common.request.AdvertisementPublishRequest;
import com.agency.ads.common.request.AdvertisementUpdateRequest;
import com.agency.ads.common.response.AdvertisementResponse;
import com.agency.ads.domain.entity.AdvertisementEntity;
import com.agency.ads.domain.entity.DealerEntity;
import com.agency.ads.domain.repository.AdvertisementRepository;
import com.agency.ads.util.Generators;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
class AdsServiceTest {

    @InjectMocks
    private AdsService adsService;

    @Mock
    private DealerService dealerService;

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private MessageSource messageSource;

    /**
     * Test to create an advertisement
     */
    @Test
    public void createAdvertisement() {

        // arrange
        DealerEntity dealerEntity = Generators.getDealerEntityResponse();
        Optional<DealerEntity> dealerEntityOp = Optional.of(dealerEntity);
        Mockito.doReturn(dealerEntityOp).when(dealerService).getDealerById(Mockito.any());

        AdvertisementEntity entity = Generators.getAdvertisementEntityResponse(dealerEntity.getId());
        Mockito.doReturn(entity).when(advertisementRepository).save(Mockito.any());

        AdvertisementCreateRequest request = new AdvertisementCreateRequest();
        request.setDealerId(dealerEntity.getId());
        request.setVehicle("Vehicle");
        request.setPrice(entity.getPrice());

        // act
        AdvertisementResponse response = adsService.create(request);

        // assert
        Assertions.assertEquals(entity.getDealerId(), response.getDealerId());
        Assertions.assertEquals(entity.getId(), response.getId());
    }

    /**
     * In this test it will trying to create an advertisement but the Dealer does not exist.
     */
    @Test
    public void createAdvertisementButDealerNotFound() {


        AdsException thrown = Assertions.assertThrows(AdsException.class, () -> {
            // arrange
            AdvertisementCreateRequest request = Generators.getAdvertisementCreateRequest();
            // act
            adsService.create(request);
        });

        Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND, thrown.getErrorCode());
    }

    /**
     * Test to update an advertisement.
     */
    @Test
    public void updateAdvertisement() {

        // arrange
        DealerEntity dealerEntity = Generators.getDealerEntityResponse();
        Optional<DealerEntity> dealerEntityOp = Optional.of(dealerEntity);
        Mockito.doReturn(dealerEntityOp).when(dealerService).getDealerById(Mockito.any());

        AdvertisementEntity entity = Generators.getAdvertisementEntityResponse(dealerEntity.getId());
        Optional<AdvertisementEntity> entityOp = Optional.of(entity);
        Mockito.doReturn(entityOp).when(advertisementRepository)
                .findByIdAndDealerId(entity.getId(), entity.getDealerId());

        AdvertisementUpdateRequest request = Generators.getAdvertisementUpdateRequest();
        request.setDealerId(dealerEntity.getId());
        request.setId(entity.getId());

        // act
        AdvertisementResponse response = adsService.update(request);

        // assert
        Assertions.assertEquals(entity.getDealerId(), response.getDealerId());
        Assertions.assertEquals(entity.getId(), response.getId());
    }

    /**
     * In this test it will trying to update an advertisement but the Dealer does not exist.
     */
    @Test
    public void updateAdvertisementButDealerNotFound() {

        AdsException thrown = Assertions.assertThrows(AdsException.class, () -> {
            // arrange
            AdvertisementUpdateRequest request = Generators.getAdvertisementUpdateRequest();
            adsService.update(request);
        });

        // assert
        Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND, thrown.getErrorCode());
    }

    /**
     * In this test it will be trying to publish an advertisement
     * but the tier limit is done and then we will receive a
     * message about that situation.
     * ERROR_MESSAGE configuration to handler this action.
     */
    @Test
    public void handlerTierLimitWithMessagePublishAdvertisement() {

        AdsException thrown = Assertions.assertThrows(AdsException.class, () -> {
            // arrange
            DealerEntity dealerEntity = Generators.getDealerEntityResponse();
            Optional<DealerEntity> dealerEntityOp = Optional.of(dealerEntity);
            Mockito.doReturn(dealerEntityOp).when(dealerService).getDealerById(Mockito.any());

            AdvertisementEntity entity = Generators.getAdvertisementEntityResponse(dealerEntity.getId());
            Optional<AdvertisementEntity> entityOp = Optional.of(entity);
            Mockito.doReturn(entityOp).when(advertisementRepository)
                    .findByIdAndDealerId(entity.getId(), entity.getDealerId());

            Mockito.doReturn(Generators.getAdvertisementEntityList())
                    .when(advertisementRepository).findByDealerIdAndStateOrderByCreatedAtAsc(
                            entity.getDealerId(), AdvertisementState.PUBLISH);


            AdvertisementPublishRequest request = new AdvertisementPublishRequest();
            request.setDealerId(dealerEntity.getId());
            request.setId(entity.getId());

            // act
            adsService.publish(request);
        });

        // assert
        Assertions.assertEquals(ErrorCode.HANDLER_LIMIT, thrown.getErrorCode());
    }

    /**
     * In this test it will be publishing an advertisement
     * but the tier limit is done then the oldest advertisement
     * it going to set the state to DRAFT.
     * UNPUBLISH_THE_OLDEST configuration to handler this action.
     */
    @Test
    public void handlerTierUnPublishTheOldest() {
        DealerEntity dealerEntity = Generators.getDealerEntityResponse();
        dealerEntity.setHandlerLimit(HandlerLimit.UNPUBLISH_THE_OLDEST);
        Optional<DealerEntity> dealerEntityOp = Optional.of(dealerEntity);
        Mockito.doReturn(dealerEntityOp).when(dealerService).getDealerById(Mockito.any());

        AdvertisementEntity entity = Generators.getAdvertisementEntityResponse(dealerEntity.getId());
        entity.setState(AdvertisementState.PUBLISH);
        Mockito.doReturn(entity).when(advertisementRepository).save(Mockito.any());
        Optional<AdvertisementEntity> entityOp = Optional.of(entity);

        Mockito.doReturn(entityOp).when(advertisementRepository)
                .findByIdAndDealerId(entity.getId(), entity.getDealerId());

        Mockito.doReturn(Generators.getAdvertisementEntityList())
                .when(advertisementRepository)
                .findByDealerIdAndStateOrderByCreatedAtAsc(
                        entity.getDealerId(),
                        AdvertisementState.PUBLISH);

        AdvertisementPublishRequest request = new AdvertisementPublishRequest();
        request.setDealerId(dealerEntity.getId());
        request.setId(entity.getId());

        Mockito.doReturn(1).when(advertisementRepository)
                .unPublishTheOldest(
                        entity.getDealerId(),
                        AdvertisementState.PUBLISH.name(),
                        AdvertisementState.DRAFT.name()
                );

        // act
        AdvertisementResponse response = adsService.publish(request);

        // assert
        Assertions.assertEquals(entity.getDealerId(), response.getDealerId());
        Assertions.assertEquals(AdvertisementState.PUBLISH, response.getState());
    }

    /**
     * This test going to publish an advertisement.
     */
    @Test
    public void publish() {
        DealerEntity dealerEntity = Generators.getDealerEntityResponse();
        dealerEntity.setTierLimit(6);
        Optional<DealerEntity> dealerEntityOp = Optional.of(dealerEntity);
        Mockito.doReturn(dealerEntityOp).when(dealerService).getDealerById(Mockito.any());

        AdvertisementEntity entity = Generators.getAdvertisementEntityResponse(dealerEntity.getId());
        entity.setState(AdvertisementState.PUBLISH);
        Mockito.doReturn(entity).when(advertisementRepository).save(Mockito.any());
        Optional<AdvertisementEntity> entityOp = Optional.of(entity);

        Mockito.doReturn(entityOp).when(advertisementRepository)
                .findByIdAndDealerId(entity.getId(), entity.getDealerId());

        Mockito.doReturn(Generators.getAdvertisementEntityList())
                .when(advertisementRepository)
                .findByDealerIdAndStateOrderByCreatedAtAsc(
                        entity.getDealerId(),
                        AdvertisementState.PUBLISH);

        AdvertisementPublishRequest request = new AdvertisementPublishRequest();
        request.setDealerId(dealerEntity.getId());
        request.setId(entity.getId());

        // act
        AdvertisementResponse response = adsService.publish(request);

        // assert
        Assertions.assertEquals(entity.getDealerId(), response.getDealerId());
        Assertions.assertEquals(AdvertisementState.PUBLISH, response.getState());
    }
}