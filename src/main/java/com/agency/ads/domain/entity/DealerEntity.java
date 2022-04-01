package com.agency.ads.domain.entity;

import com.agency.ads.common.enums.HandlerLimit;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "dealer")
public class DealerEntity {

    @Id
    @Column
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column
    private String name;

    @Column(name = "ads_limit")
    private Integer tierLimit;

    @Enumerated(EnumType.STRING)
    @Column(name = "handler_limit")
    private HandlerLimit handlerLimit;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTierLimit() {
        return tierLimit;
    }

    public void setTierLimit(Integer tierLimit) {
        this.tierLimit = tierLimit;
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

    public HandlerLimit getHandlerLimit() {
        return handlerLimit;
    }

    public void setHandlerLimit(HandlerLimit handlerLimit) {
        this.handlerLimit = handlerLimit;
    }
}
