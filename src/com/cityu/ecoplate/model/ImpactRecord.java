package com.cityu.ecoplate.model;

import java.time.LocalDateTime;

public record ImpactRecord(LocalDateTime time, String batchId, String actionType,
                           double divertedKg, double mealsEquivalent, double co2AvoidedKg) {}
