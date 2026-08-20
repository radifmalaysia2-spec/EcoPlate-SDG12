package com.cityu.ecoplate.service;

import com.cityu.ecoplate.model.FoodBatch;
import java.util.Random;

public class DemandSimulator {
    private final Random random;
    public DemandSimulator(long seed) { random = new Random(seed); }
    public double simulateRemaining(FoodBatch batch, int expectedCustomers, String weather) {
        double weatherFactor = switch (weather) { case "Rainy" -> 0.82; case "Hot" -> 0.92; default -> 1.0; };
        double demandKg = expectedCustomers * 0.42 * weatherFactor * (0.9 + random.nextDouble() * 0.2);
        return Math.max(0, batch.getPreparedKg() - demandKg);
    }
}
