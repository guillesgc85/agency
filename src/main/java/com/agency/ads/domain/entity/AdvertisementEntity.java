package com.agency.ads.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.agency.ads.common.enums.AdvertisementState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import java.util.UUID;

@Entity
@Table(name = "advertisement")
public class AdvertisementEntity {

    @Id
    @Column
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "dealer_id")
    private UUID dealerId;

    @Column
    private String vehicle;

    @Column
    private String price;

    @Enumerated(EnumType.STRING)
    @Column
    private AdvertisementState state = AdvertisementState.DRAFT;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime updateAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDealerId() {
        return dealerId;
    }

    public void setDealerId(UUID dealerId) {
        this.dealerId = dealerId;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public AdvertisementState getState() {
        return state;
    }

    public void setState(AdvertisementState state) {
        this.state = state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
