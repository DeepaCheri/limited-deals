package com.ecommerce.limitedtimedeals.models;

import lombok.Data;
import lombok.Generated;

import java.time.LocalDateTime;
import java.util.HashSet;

@Data
public class Deal {
    private static final int DEFAULT_DURATION_IN_HOURS = 2;
    private static final boolean ACTIVE = true;
    @Generated
    private long id;
    private double price;
    private String itemName;
    private int count;
    private int duration;
    private final HashSet<Integer> purchasedUsers;
    private LocalDateTime endTime;


    public Deal(double price, String itemName, int count) {
        this.price = price;
        this.itemName = itemName;
        this.count = count;
        this.purchasedUsers = new HashSet<>();
        this.duration = DEFAULT_DURATION_IN_HOURS;
        this.endTime = LocalDateTime.now()
                .plusHours(duration);
        this.status = ACTIVE;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private boolean status = ACTIVE;


    public boolean isEligibleToClaim(int userId) {
        return status == ACTIVE && count != 0 && !purchasedUsers.contains(userId)
                && LocalDateTime.now().isBefore(endTime);
    }

}
