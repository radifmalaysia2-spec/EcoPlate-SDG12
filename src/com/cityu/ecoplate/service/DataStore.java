package com.cityu.ecoplate.service;

import com.cityu.ecoplate.model.*;
import java.util.List;

public interface DataStore {
    void save(List<FoodBatch> batches, List<ImpactRecord> impacts);
    StoredData load();
    record StoredData(List<FoodBatch> batches, List<ImpactRecord> impacts) {}
}
