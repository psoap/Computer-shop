package com.epam.computershop.enums;

public enum OrderStatus {
    BASKET((short)1),
    PAID((short)2),
    SHIPPING((short)3),
    DELIVERED((short)4);

    private final short STATUS_ID;

    OrderStatus(short statusId) {
        this.STATUS_ID = statusId;
    }

    public short getStatusId() {
        return STATUS_ID;
    }

    public static OrderStatus getStatusById(int id) {
        OrderStatus resultStatus = BASKET;
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getStatusId() == id) {
                resultStatus = status;
            }
        }
        return resultStatus;
    }
}