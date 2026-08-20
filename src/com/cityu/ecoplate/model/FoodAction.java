package com.cityu.ecoplate.model;

public abstract class FoodAction {
    private final FoodBatch batch;
    private final double eligibleKg;
    protected FoodAction(FoodBatch batch, double eligibleKg) { this.batch = batch; this.eligibleKg = Math.max(0, eligibleKg); }
    public FoodBatch getBatch() { return batch; }
    public double getEligibleKg() { return eligibleKg; }
    public abstract String getActionName();
    public abstract String explain();
    public abstract ImpactRecord execute();
}
