package com.cityu.ecoplate.service;

import com.cityu.ecoplate.model.*;
import java.time.LocalDateTime;
import java.util.*;

public class EcoPlateService {
    private final List<FoodBatch> batches = new ArrayList<>();
    private final List<ImpactRecord> impacts = new ArrayList<>();
    private final Map<FoodCategory, Double> wasteByCategory = new HashMap<>();
    private final DataStore store;
    private final WasteStrategy strategy = new SafetyFirstStrategy();
    private final DemandSimulator simulator = new DemandSimulator(12);
    public EcoPlateService(DataStore store) { this.store = store; }
    public void load() {
        DataStore.StoredData data = store.load(); batches.addAll(data.batches()); impacts.addAll(data.impacts());
        if (batches.isEmpty()) seedDemoData(); recalculate();
    }
    private void seedDemoData() {
        batches.add(new FoodBatch("B-101", "Chicken Rice", FoodCategory.RICE, 18, 5.4, LocalDateTime.now().plusHours(5)));
        batches.add(new FoodBatch("B-102", "Vegetable Curry", FoodCategory.VEGETABLE, 12, 2.1, LocalDateTime.now().plusHours(3)));
        batches.add(new FoodBatch("B-103", "Bread Rolls", FoodCategory.BAKERY, 8, 3.2, LocalDateTime.now().plusHours(24)));
    }
    public void addBatch(FoodBatch batch) { batches.add(batch); recalculate(); save(); }
    public void simulate(int customers, String weather) { for (FoodBatch b : batches) b.setRemainingKg(Math.min(b.getPreparedKg(), simulator.simulateRemaining(b, customers, weather))); recalculate(); save(); }
    public FoodAction recommendationFor(FoodBatch batch) { return strategy.recommend(batch); }
    public ImpactRecord apply(FoodAction action) { ImpactRecord result = action.execute(); impacts.add(result); recalculate(); save(); return result; }
    public void save() { store.save(batches, impacts); }
    private void recalculate() { wasteByCategory.clear(); for (FoodBatch b : batches) wasteByCategory.merge(b.getCategory(), b.getRemainingKg(), Double::sum); }
    public List<FoodBatch> getBatches() { return Collections.unmodifiableList(batches); }
    public List<ImpactRecord> getImpacts() { return Collections.unmodifiableList(impacts); }
    public Map<FoodCategory, Double> getWasteByCategory() { return Collections.unmodifiableMap(wasteByCategory); }
    public double totalPrepared() { return batches.stream().mapToDouble(FoodBatch::getPreparedKg).sum(); }
    public double totalRemaining() { return batches.stream().mapToDouble(FoodBatch::getRemainingKg).sum(); }
    public double totalDiverted() { return impacts.stream().mapToDouble(ImpactRecord::divertedKg).sum(); }
    public double totalCo2Avoided() { return impacts.stream().mapToDouble(ImpactRecord::co2AvoidedKg).sum(); }
}
