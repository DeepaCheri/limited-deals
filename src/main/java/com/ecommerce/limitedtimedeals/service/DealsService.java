package com.ecommerce.limitedtimedeals.service;

import com.ecommerce.limitedtimedeals.exception.DealException;
import com.ecommerce.limitedtimedeals.models.Deal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DealsService {

    private final HashMap<Integer, List<Deal>> sellerDeals;

    public DealsService() {
        this.sellerDeals = new HashMap<>();
    }

    public Deal addDeal(int sellerId, Deal deal) {
        List<Deal> deals = this.sellerDeals.getOrDefault(sellerId, new ArrayList<>());
        deals.add(deal);
        this.sellerDeals.put(sellerId, deals);
        return deal;
    }

    public Optional<Deal> endDeal(int sellerId, int dealId) {
        List<Deal> deals = this.sellerDeals.getOrDefault(sellerId, new ArrayList<>());
        Optional<Deal> deal = deals.stream()
                .filter(dealInstance -> dealInstance.getId() == dealId)
                .findFirst();
        deal.ifPresent(value -> value.setStatus(false));
        return deal;
    }

    public Optional<Deal> updateDeal(int sellerId, int dealId, Deal newDeal) {
        List<Deal> deals = this.sellerDeals.getOrDefault(sellerId, new ArrayList<>());
        Optional<Deal> deal = deals.stream()
                .filter(dealInstance -> dealInstance.getId() == dealId)
                .findFirst();
        if (deal.isPresent()) {
            if (newDeal.getDuration() > 2) {
                deal.get()
                        .setDuration(newDeal.getDuration());
                deal.get()
                        .setEndTime(deal.get()
                                .getEndTime()
                                .plusHours(newDeal.getDuration() - 2));
            }
            if (newDeal.getCount() > 0) {
                int count = deal.get()
                        .getCount();
                deal.get()
                        .setCount(count + newDeal.getCount());
            }
        }
        return deal;
    }

    public Optional<Deal> claimDeal(int userId, int dealId) {
        Optional<Deal> deal = Optional.empty();

        for (Map.Entry<Integer, List<Deal>> sellerDeal : sellerDeals.entrySet()) {
            deal = sellerDeal.getValue()
                    .stream()
                    .filter(dealInstance -> dealInstance.getId() == dealId)
                    .findFirst();
            if (deal.isPresent()) {
                break;
            }
        }
        if (!deal.isPresent()) {
            throw new DealException(String.format("Deal with given id %s not found", dealId));
        }
        final Deal dealToClaim = deal.get();
        boolean eligibleToClaim = dealToClaim.isEligibleToClaim(userId);
        if (eligibleToClaim) {
            dealToClaim.setCount(dealToClaim.getCount() - 1);
            dealToClaim.getPurchasedUsers()
                    .add(userId);
        } else {
            throw new DealException("Deals are closed or not eligible to claim");
        }
        return deal;
    }
}
