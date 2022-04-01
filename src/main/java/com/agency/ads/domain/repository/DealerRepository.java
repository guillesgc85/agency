package com.agency.ads.domain.repository;

import com.agency.ads.domain.entity.DealerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DealerRepository extends JpaRepository<DealerEntity, UUID> {

}
