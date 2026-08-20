package com.cityu.ecoplate;

import com.cityu.ecoplate.model.*;
import com.cityu.ecoplate.service.*;
import java.time.LocalDateTime;

public class EcoPlateTest {
    public static void main(String[] args) {
        FoodBatch safe = new FoodBatch("T1", "Test Meal", FoodCategory.OTHER, 10, 4, LocalDateTime.now().plusHours(5));
        assert safe.consumedKg() == 6;
        assert new SafetyFirstStrategy().recommend(safe) instanceof DonationAction;
        FoodAction action = new SafetyFirstStrategy().recommend(safe);
        ImpactRecord result = action.execute();
        assert result.divertedKg() == 4;
        assert safe.getRemainingKg() == 0;
        boolean invalidRejected = false;
        try { new FoodBatch("T2", "Bad", FoodCategory.OTHER, 2, 3, LocalDateTime.now()); } catch (IllegalArgumentException ex) { invalidRejected = true; }
        assert invalidRejected;
        System.out.println("All EcoPlate domain tests passed.");
    }
}
