package com.cityu.ecoplate.model;

import java.time.LocalDateTime;

public class CompostAction extends FoodAction {
    public CompostAction(FoodBatch batch, double kg) { super(batch, kg); }
    @Override public String getActionName() { return "Compost"; }
    @Override public String explain() { return "The safety window is short or expired. Divert the batch from landfill to composting."; }
    @Override public ImpactRecord execute() {
        double kg = Math.min(getEligibleKg(), getBatch().getRemainingKg());
        getBatch().setRemainingKg(getBatch().getRemainingKg() - kg);
        return new ImpactRecord(LocalDateTime.now(), getBatch().getId(), getActionName(), kg, 0, kg * 0.6);
    }
}
