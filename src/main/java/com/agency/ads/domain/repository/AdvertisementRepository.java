package com.agency.ads.domain.repository;

import com.agency.ads.common.enums.AdvertisementState;
import com.agency.ads.domain.entity.AdvertisementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdvertisementRepository extends JpaRepository<AdvertisementEntity, UUID> {

    List<AdvertisementEntity> findByDealerIdAndStateOrderByCreatedAtAsc(UUID dealerId, AdvertisementState State);

    Optional<AdvertisementEntity> findByIdAndDealerId(UUID id, UUID dealerId);

    @Modifying
    @Query(
            value = "UPDATE advertisement " +
                    " SET state = :state, " +
                    " updated_at = now() " +
                    " WHERE dealer_id = :dealerId " +
                    " AND id = (SELECT id FROM advertisement " +
                    "WHERE dealer_id = :dealerId " +
                    " AND  state = :publishState ORDER BY created_at ASC LIMIT 1)"
            , nativeQuery = true
    )
    int unPublishTheOldest(
            @Param(value = "dealerId") UUID leaderId,
            @Param(value = "publishState") String publishState,
            @Param(value = "state") String state
    );
}
