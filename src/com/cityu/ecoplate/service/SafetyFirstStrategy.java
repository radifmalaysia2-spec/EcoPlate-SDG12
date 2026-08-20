package com.cityu.ecoplate.service;

import com.cityu.ecoplate.model.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class SafetyFirstStrategy implements WasteStrategy {
    @Override public FoodAction recommend(FoodBatch batch) {
        long hours = Duration.between(LocalDateTime.now(), batch.getSafeUntil()).toHours();
        double kg = batch.getRemainingKg();
        return hours >= 2 ? new DonationAction(batch, kg) : new CompostAction(batch, kg);
    }
}
