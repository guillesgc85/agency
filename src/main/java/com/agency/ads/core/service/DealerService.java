package com.agency.ads.core.service;

import com.agency.ads.common.exception.AdsException;
import com.agency.ads.common.request.DealerHandlerLimitRequest;
import com.agency.ads.core.mapper.DealerMapper;
import com.agency.ads.common.request.DealerRequest;
import com.agency.ads.common.response.DealerResponse;
import com.agency.ads.domain.entity.DealerEntity;
import com.agency.ads.domain.repository.DealerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.agency.ads.common.constant.ErrorCode.ITEM_NOT_FOUND;

/**
 * DealerService
 */
@Service
public class DealerService {

    private static final String CLASS = DealerService.class.getSimpleName();
    private Logger logger = LoggerFactory.getLogger(DealerService.class);

    private MessageSource messageSource;
    private DealerRepository dealerRepository;

    @Autowired
    public DealerService(
            MessageSource messageSource,
            DealerRepository dealerRepository
    ){
        this.messageSource = messageSource;
        this.dealerRepository = dealerRepository;
    }

    /**
     * Create a new Dealer
     * @param request
     * @return DealerResponse
     */
    public DealerResponse create(DealerRequest request){
        DealerEntity entity = DealerMapper.requestToCreateEntity(request);
        dealerRepository.save(entity);
        return DealerMapper.entityToResponse(entity);
    }

    /**
     * get Dealer by Id
     * @param id
     * @return Optional<DealerEntity>
     */
    public Optional<DealerEntity> getDealerById(UUID id) {
        return dealerRepository.findById(id);
    }

    /**
     *
     * @param dealerId
     * @param handlerLimit
     * @return DealerResponse
     */
    public DealerResponse updateHandlerLimit(UUID dealerId, DealerHandlerLimitRequest handlerLimit){
        Optional<DealerEntity> dealerOptional = getDealerById(dealerId);
        if(!dealerOptional.isPresent()){
            throw itemNotFound();
        }
        DealerEntity entity = dealerOptional.get();
        DealerMapper.setHandlerLimit(entity, handlerLimit.getHandlerLimit());
        dealerRepository.save(entity);
        return DealerMapper.entityToResponse(entity);
    }

    /**
     * AdsException whit ITEM_NOT_FOUND code
     * @return AdsException
     */
    private AdsException itemNotFound(){
        String message = messageSource.getMessage(ITEM_NOT_FOUND, null, LocaleContextHolder.getLocale());
        return new AdsException(HttpStatus.BAD_REQUEST.value(), ITEM_NOT_FOUND, message);
    }
}
