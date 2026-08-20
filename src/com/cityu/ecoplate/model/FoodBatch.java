package com.cityu.ecoplate.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class FoodBatch {
    private final String id;
    private String name;
    private FoodCategory category;
    private double preparedKg;
    private double remainingKg;
    private LocalDateTime safeUntil;

    public FoodBatch(String id, String name, FoodCategory category, double preparedKg,
                     double remainingKg, LocalDateTime safeUntil) {
        this.id = Objects.requireNonNull(id);
        setName(name); setCategory(category); setPreparedKg(preparedKg);
        setRemainingKg(remainingKg); setSafeUntil(safeUntil);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String value) { if (value == null || value.isBlank()) throw new IllegalArgumentException("Food name is required"); name = value.trim(); }
    public FoodCategory getCategory() { return category; }
    public void setCategory(FoodCategory value) { category = Objects.requireNonNull(value); }
    public double getPreparedKg() { return preparedKg; }
    public void setPreparedKg(double value) { if (value <= 0) throw new IllegalArgumentException("Prepared amount must be above zero"); preparedKg = value; }
    public double getRemainingKg() { return remainingKg; }
    public void setRemainingKg(double value) { if (value < 0 || value > preparedKg) throw new IllegalArgumentException("Remaining amount must be between zero and prepared amount"); remainingKg = value; }
    public LocalDateTime getSafeUntil() { return safeUntil; }
    public void setSafeUntil(LocalDateTime value) { safeUntil = Objects.requireNonNull(value); }
    public double consumedKg() { return preparedKg - remainingKg; }
    public double wasteRatio() { return remainingKg / preparedKg; }
}
