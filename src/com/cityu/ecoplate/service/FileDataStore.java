package com.cityu.ecoplate.service;

import com.cityu.ecoplate.model.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class FileDataStore implements DataStore {
    private final Path path;
    public FileDataStore(String filename) { path = Paths.get(filename); }
    @Override public void save(List<FoodBatch> batches, List<ImpactRecord> impacts) {
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (BufferedWriter w = Files.newBufferedWriter(path)) {
                for (FoodBatch b : batches) w.write(String.join("|", "B", b.getId(), b.getName().replace("|", "/"), b.getCategory().name(), String.valueOf(b.getPreparedKg()), String.valueOf(b.getRemainingKg()), b.getSafeUntil().toString()) + "\n");
                for (ImpactRecord i : impacts) w.write(String.join("|", "I", i.time().toString(), i.batchId(), i.actionType(), String.valueOf(i.divertedKg()), String.valueOf(i.mealsEquivalent()), String.valueOf(i.co2AvoidedKg())) + "\n");
            }
        } catch (IOException e) { throw new IllegalStateException("Unable to save application data", e); }
    }
    @Override public StoredData load() {
        List<FoodBatch> batches = new ArrayList<>(); List<ImpactRecord> impacts = new ArrayList<>();
        if (!Files.exists(path)) return new StoredData(batches, impacts);
        try {
            for (String line : Files.readAllLines(path)) {
                String[] p = line.split("\\|", -1);
                if (p[0].equals("B") && p.length == 7) batches.add(new FoodBatch(p[1], p[2], FoodCategory.valueOf(p[3]), Double.parseDouble(p[4]), Double.parseDouble(p[5]), LocalDateTime.parse(p[6])));
                if (p[0].equals("I") && p.length == 7) impacts.add(new ImpactRecord(LocalDateTime.parse(p[1]), p[2], p[3], Double.parseDouble(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6])));
            }
        } catch (Exception e) { throw new IllegalStateException("Saved data is invalid", e); }
        return new StoredData(batches, impacts);
    }
}
