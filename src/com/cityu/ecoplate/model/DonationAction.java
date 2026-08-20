package com.cityu.ecoplate.model;

import java.time.LocalDateTime;

public class DonationAction extends FoodAction {
    public DonationAction(FoodBatch batch, double kg) { super(batch, kg); }
    @Override public String getActionName() { return "Redistribute"; }
    @Override public String explain() { return "Food is still safe. Prioritise redistribution to the campus community fridge."; }
    @Override public ImpactRecord execute() {
        double kg = Math.min(getEligibleKg(), getBatch().getRemainingKg());
        getBatch().setRemainingKg(getBatch().getRemainingKg() - kg);
        return new ImpactRecord(LocalDateTime.now(), getBatch().getId(), getActionName(), kg, kg / 0.45, kg * 2.5);
    }
}
