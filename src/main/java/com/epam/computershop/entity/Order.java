package com.epam.computershop.entity;

import com.epam.computershop.enums.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order implements Entity, Serializable {
    private long id;
    private long userId;
    private OrderStatus status;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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
