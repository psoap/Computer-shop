package com.epam.computershop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order implements Entity, Serializable {
    private long id;
    private long userId;
    private short statusId;
    private Long deliveryProfileId;
    private BigDecimal totalPrice;
    private Timestamp changeDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public short getStatusId() {
        return statusId;
    }

    public void setStatusId(short statusId) {
        this.statusId = statusId;
    }

    public Long getDeliveryProfileId() {
        return deliveryProfileId;
    }

    public void setDeliveryProfileId(Long deliveryProfileId) {
        this.deliveryProfileId = deliveryProfileId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Timestamp changeDate) {
        this.changeDate = changeDate;
    }
}
