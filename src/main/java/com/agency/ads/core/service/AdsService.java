package com.agency.ads.core.service;

import com.agency.ads.common.enums.AdvertisementState;
import com.agency.ads.common.enums.HandlerLimit;
import com.agency.ads.common.exception.AdsException;
import com.agency.ads.common.request.AdvertisementPublishRequest;
import com.agency.ads.common.request.AdvertisementUpdateRequest;
import com.agency.ads.core.mapper.AdvertisementMapper;
import com.agency.ads.common.request.AdvertisementCreateRequest;
import com.agency.ads.common.response.AdvertisementResponse;
import com.agency.ads.domain.entity.AdvertisementEntity;
import com.agency.ads.domain.entity.DealerEntity;
import com.agency.ads.domain.repository.AdvertisementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.agency.ads.common.constant.ErrorCode.HANDLER_LIMIT;
import static com.agency.ads.common.constant.ErrorCode.ITEM_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.StampedLock;

/**
 * Service to management advertisement
 */
@Service
public class AdsService {

    private static final String CLASS = AdsService.class.getSimpleName();
    private Logger logger = LoggerFactory.getLogger(AdsService.class);

    private MessageSource messageSource;
    private DealerService dealerService;
    private AdvertisementRepository advertisementRepository;
    private StampedLock lock;

    /**
     * AdsService constructor
     * @param messageSource
     * @param dealerService
     * @param advertisementRepository
     */
    @Autowired
    public AdsService(
            MessageSource messageSource,
            DealerService dealerService,
            AdvertisementRepository advertisementRepository
    ) {
        this.messageSource = messageSource;
        this.dealerService = dealerService;
        this.advertisementRepository = advertisementRepository;
        this.lock = new StampedLock();
    }

    /**
     * Create a new advertisement
     * @param request
     * @return AdvertisementResponse
     */
    @Transactional
    public AdvertisementResponse create(AdvertisementCreateRequest request) {
        dealerService.getDealerById(request.getDealerId()).orElseThrow(() -> itemNotFound());
        return saveAds(request);
    }

    /**
     * Update an advertisement
     * @param request
     * @return AdvertisementResponse
     */
    @Transactional
    public AdvertisementResponse update(AdvertisementUpdateRequest request) {
        AdvertisementEntity advertisementFound = advertisementRepository
                .findByIdAndDealerId(request.getId(), request.getDealerId()).orElseThrow(() -> itemNotFound());
        return updateAds(request, advertisementFound);

    }

    /**
     * Allow publishing an advertisement.
     * @param request
     * @return AdvertisementResponse or and AdsException
     * @throws AdsException
     */
    @Transactional
    public AdvertisementResponse publish(AdvertisementPublishRequest request) throws AdsException {
        AdvertisementEntity advertisementFound = advertisementRepository.findByIdAndDealerId(
                request.getId(), request.getDealerId()).orElseThrow(() -> itemNotFound());

        DealerEntity dealerFound = dealerService.getDealerById(request.getDealerId()).orElseThrow(() -> itemNotFound());
        int adsPublished = getNumberOfAdsPublishedByDealerIdAndState(dealerFound.getId(), AdvertisementState.PUBLISH);
        if (adsPublished >= dealerFound.getTierLimit()) {
            limitHandler(dealerFound.getHandlerLimit(), dealerFound.getId());
        }
        return publishing(advertisementFound);
    }

    /**
     * Allow unPublish an advertisement.
     * @param request
     * @return AdvertisementResponse
     */
    @Transactional
    public AdvertisementResponse unPublish(AdvertisementPublishRequest request) {
        AdvertisementEntity advertisementFound = advertisementRepository.findByIdAndDealerId(
                request.getId(), request.getDealerId()).orElseThrow(() -> itemNotFound());
        return unPublished(advertisementFound);
    }

    /**
     * Get all AdvertisementEntity by dealer id and state.
     * @param dealerId
     * @param state
     * @return List<AdvertisementEntity>
     */
    public List<AdvertisementEntity> getAdsByDealerIdAndState(UUID dealerId, AdvertisementState state) {
        return advertisementRepository.findByDealerIdAndStateOrderByCreatedAtAsc(dealerId, state);
    }

    /**
     * When
     * @return AdsException
     */
    private AdsException itemNotFound() {
        String message = messageSource.getMessage(ITEM_NOT_FOUND, null, LocaleContextHolder.getLocale());
        return new AdsException(HttpStatus.BAD_REQUEST.value(), ITEM_NOT_FOUND, message);
    }

    /**
     * When the tier limit is filled this method allow choosing the way to follow
     * @param handlerLimit
     * @param leaderId
     */
    private void limitHandler(HandlerLimit handlerLimit, UUID leaderId) {
        switch (handlerLimit) {
            case ERROR_MESSAGE:
                errorMessageLimitHandler();
                break;
            case UNPUBLISH_THE_OLDEST:
                unPublishTheOldestLimitHandler(leaderId);
                break;
        }
    }

    /**
     * Throw new an AdsException when handle tier is through ERROR_MESSAGE
     */
    private void errorMessageLimitHandler() {
        String message = messageSource.getMessage(HANDLER_LIMIT, null, LocaleContextHolder.getLocale());
        throw new AdsException(HttpStatus.BAD_REQUEST.value(), HANDLER_LIMIT, message);
    }

    /**
     * Allow to unPublish the oldest Advertisement
     * @param leaderId
     */
    @Transactional
    private void unPublishTheOldestLimitHandler(UUID leaderId) {
        long stamp = lock.writeLock();
        try {
            advertisementRepository.unPublishTheOldest(
                    leaderId, AdvertisementState.PUBLISH.name(), AdvertisementState.DRAFT.name());
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * Persist a rew AdvertisementEntity
     * @param request
     * @return AdvertisementResponse
     */
    private AdvertisementResponse saveAds(AdvertisementCreateRequest request) {
        AdvertisementEntity entity = AdvertisementMapper.requestToCreateEntity(request);
        advertisementRepository.save(entity);
        return AdvertisementMapper.entityToResponse(entity);
    }

    /**
     * Update an exist AdvertisementEntity.
     * @param request
     * @param advertisementFound
     * @return AdvertisementResponse
     */
    private AdvertisementResponse updateAds(AdvertisementUpdateRequest request, AdvertisementEntity advertisementFound) {
        AdvertisementEntity entity = AdvertisementMapper.requestToUpdateEntity(request, advertisementFound);
        advertisementRepository.save(entity);
        return AdvertisementMapper.entityToResponse(entity);
    }

    /**
     * Allow publish or unPublish an advertisement.
     * @param advertisementFound
     * @param state
     * @return AdvertisementResponse
     */
    private AdvertisementResponse publishOrUnPublish(AdvertisementEntity advertisementFound, AdvertisementState state) {
        long stamp = lock.writeLock();
        try {
            AdvertisementEntity entity = AdvertisementMapper.publishToUpdateEntity(advertisementFound, state);
            advertisementRepository.save(entity);
            return AdvertisementMapper.entityToResponse(entity);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * Allow publish an advertisement.
     * @param advertisementFound
     * @return AdvertisementResponse
     */
    private AdvertisementResponse publishing(AdvertisementEntity advertisementFound) {
        return publishOrUnPublish(advertisementFound, AdvertisementState.PUBLISH);
    }

    /**
     * Allow unPublished an advertisement.
     * @param advertisementFound
     * @return AdvertisementResponse
     */
    private AdvertisementResponse unPublished(AdvertisementEntity advertisementFound) {
        return publishOrUnPublish(advertisementFound, AdvertisementState.DRAFT);
    }

    /**
     * Get the number of advertisements by dealer_id and state to know that if possible to publish
     * an advertisement
     * @param dealerId
     * @param state
     * @return number of advertisements
     */
    private int getNumberOfAdsPublishedByDealerIdAndState(UUID dealerId, AdvertisementState state) {
        return getAdsByDealerIdAndStateLock(dealerId, state).size();
    }

    /**
     * Get list of advertisements by dealer_id and state.
     * @param dealerId
     * @param state
     * @return List<AdvertisementEntity>
     */
    private List<AdvertisementEntity> getAdsByDealerIdAndStateLock(UUID dealerId, AdvertisementState state) {
        long stamp = lock.readLock();
        try {
            return advertisementRepository.findByDealerIdAndStateOrderByCreatedAtAsc(dealerId, state);
        } finally {
            lock.unlockRead(stamp);
        }
    }
}
