package com.agency.ads.app.controller;

import com.agency.ads.app.Application;
import com.agency.ads.common.constant.ErrorCode;
import com.agency.ads.common.constant.Header;
import com.agency.ads.common.enums.AdvertisementState;
import com.agency.ads.common.enums.LanguageEnum;
import com.agency.ads.common.request.AdvertisementPublishRequest;
import com.agency.ads.common.response.AdvertisementResponse;
import com.agency.ads.common.response.ErrorResponse;
import com.agency.ads.common.route.Route;
import com.agency.ads.domain.entity.AdvertisementEntity;
import com.agency.ads.domain.repository.AdvertisementRepository;
import com.agency.ads.util.Generators;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback
@Transactional
class AdsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    /**
     * Integration test to create an advertisement via http resquest
     */
    @Sql(scripts = {"file:src/test/resources/sql/dealer.sql"})
    @Test
    public void createAdvertisement() throws Exception {

        // arrange
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writeValueAsString(Generators.getAdvertisementCreateRequest());
        MvcResult result = mvc.perform(
            MockMvcRequestBuilders.post(Route.ADS)
                .content(jsonValue)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        AdvertisementResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), AdvertisementResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getCreatedAt());
        Assertions.assertEquals(Generators.DEALER_ID, response.getDealerId());
        Assertions.assertEquals(AdvertisementState.DRAFT , response.getState());
    }

    /**
     * Integration test to going to publish an advertisement through an HTTP request.
     */
    @Sql(scripts = {"file:src/test/resources/sql/dealer.sql","file:src/test/resources/sql/advertisement_draft.sql"})
    @Test
    public void publishAdvertisement() throws Exception {

        // arrange
        AdvertisementPublishRequest advertisementPublishRequest = new AdvertisementPublishRequest();
        advertisementPublishRequest.setDealerId(Generators.DEALER_ID);
        advertisementPublishRequest.setId(UUID.fromString("9c5446aa-7db7-4d3b-a6e4-b1a2646ac090"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writeValueAsString(advertisementPublishRequest);

        // act
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(Route.ADS + Route.PUBLISH)
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        AdvertisementResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), AdvertisementResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUpdateAt());
        Assertions.assertEquals(Generators.DEALER_ID, response.getDealerId());
        Assertions.assertEquals(AdvertisementState.PUBLISH , response.getState());
    }

    /**
     * Integration test to going to unpublish an advertisement through an HTTP request.
     */
    @Sql(scripts = {"file:src/test/resources/sql/dealer.sql","file:src/test/resources/sql/advertisement_published.sql"})
    @Test
    public void unPublishAdvertisement() throws Exception {

        // arrange
        AdvertisementPublishRequest advertisementPublishRequest = new AdvertisementPublishRequest();
        advertisementPublishRequest.setDealerId(Generators.DEALER_ID);
        advertisementPublishRequest.setId(UUID.fromString("9c5446aa-7db7-4d3b-a6e4-b1a2646ac090"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writeValueAsString(advertisementPublishRequest);

        // act
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(Route.ADS + Route.UNPUBLISH)
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // assert
        AdvertisementResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), AdvertisementResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUpdateAt());
        Assertions.assertEquals(Generators.DEALER_ID, response.getDealerId());
        Assertions.assertEquals(AdvertisementState.DRAFT , response.getState());
    }

    /**
     * Integration test to going to publish an advertisement but the tier limit
     * is full with handler by UNPUBLISH_THE_OLDEST.
     */
    @Sql(scripts = {"file:src/test/resources/sql/dealer.sql",
            "file:src/test/resources/sql/advertisement_published_list.sql",
            "file:src/test/resources/sql/advertisement_draft.sql"})@Test
    public void handlerTierLimitByTheOldest() throws Exception {

        // arrange
        UUID advertisementId = UUID.fromString("9c5446aa-7db7-4d3b-a6e4-b1a2646ac090");
        AdvertisementPublishRequest advertisementPublishRequest = new AdvertisementPublishRequest();
        advertisementPublishRequest.setDealerId(Generators.DEALER_ID);
        advertisementPublishRequest.setId(advertisementId);

        AdvertisementEntity advertisementEntity = advertisementRepository.findById(advertisementId).get();
        Assertions.assertEquals(AdvertisementState.DRAFT, advertisementEntity.getState());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writeValueAsString(advertisementPublishRequest);

        // act
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(Route.ADS + Route.PUBLISH)
                        .header(Header.LANGUAGE, LanguageEnum.FR.lang)
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        AdvertisementResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), AdvertisementResponse.class);

        // assert
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUpdateAt());
        Assertions.assertEquals(Generators.DEALER_ID, response.getDealerId());
        Assertions.assertEquals(AdvertisementState.PUBLISH , response.getState());
    }

    /**
     * Integration test to going to publish an advertisement but the tier limit
     * is full with handler by ERROR_MESSAGE.
     */
    @Sql(scripts = {"file:src/test/resources/sql/dealer_handler_limit_by_message.sql",
            "file:src/test/resources/sql/advertisement_published_list.sql",
            "file:src/test/resources/sql/advertisement_draft.sql"})@Test
    public void handlerTierLimitByMessage() throws Exception {

        // arrange
        AdvertisementPublishRequest advertisementPublishRequest = new AdvertisementPublishRequest();
        advertisementPublishRequest.setDealerId(Generators.DEALER_ID);

        UUID advertisementId = UUID.fromString("9c5446aa-7db7-4d3b-a6e4-b1a2646ac090");
        advertisementPublishRequest.setId(advertisementId);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writeValueAsString(advertisementPublishRequest);

        // act
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(Route.ADS + Route.PUBLISH)
                        .header(Header.LANGUAGE, LanguageEnum.FR.lang)
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ErrorResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);

        // assert
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getError());
        Assertions.assertNotNull(response.getError().getMessage());
        Assertions.assertEquals(ErrorCode.HANDLER_LIMIT, response.getError().getCode());
    }

    /**
     * Integration test to get advertisement by leader_id and state
     */
    @Sql(scripts = {"file:src/test/resources/sql/dealer.sql",
            "file:src/test/resources/sql/advertisement_published_list.sql",
    })
    @Test
    public void getAdvertisementByState() throws Exception {

        // arrange
        AdvertisementPublishRequest advertisementPublishRequest = new AdvertisementPublishRequest();
        advertisementPublishRequest.setDealerId(Generators.DEALER_ID);

        UUID advertisementId = UUID.fromString("9c5446aa-7db7-4d3b-a6e4-b1a2646ac090");
        advertisementPublishRequest.setId(advertisementId);

        String url = Route.ADS + Route.GET_BY_ID_DEALER_AND_STAGE
                .replace("{dealer_id}", Generators.DEALER_ID.toString())
                .replace("{state}", AdvertisementState.PUBLISH.toString());

        // act
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<AdvertisementEntity> response =
                objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        // assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(5, response.size());
    }
}