package com.ecommerce.limitedtimedeals.controller;

import com.ecommerce.limitedtimedeals.models.Deal;
import com.ecommerce.limitedtimedeals.service.DealsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class DealsController {

    private final DealsService dealsService;

    public DealsController(DealsService dealsService) {
        this.dealsService = dealsService;
    }

    @PostMapping("{sellerId}/deals")
    public Deal createDeal(@PathVariable int sellerId, @RequestBody Deal deal) {
        return dealsService.addDeal(sellerId, deal);
    }


    @PostMapping("{sellerId}/endDeals/{dealId}")
    public Optional<Deal> endDeal(@PathVariable int sellerId, @PathVariable int dealId) {
        return dealsService.endDeal(sellerId, dealId);
    }

    @PutMapping("{sellerId}/deals/{dealId}")
    public Optional<Deal> updateDeal(@PathVariable int sellerId, @PathVariable int dealId, @RequestBody Deal deal) {
        return dealsService.updateDeal(sellerId, dealId, deal);
    }

    @PostMapping("{userId}/claimdeals/{dealId}")
    public Optional<Deal> claimDeal(@PathVariable int userId, @PathVariable int dealId){
        return dealsService.claimDeal(userId, dealId);
    }


}
