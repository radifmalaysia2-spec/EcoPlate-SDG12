package com.cityu.ecoplate;

import com.cityu.ecoplate.service.*;
import com.cityu.ecoplate.ui.MainFrame;
import javax.swing.SwingUtilities;

public final class App {
    private App() {}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataStore store = new FileDataStore("data/ecoplate-data.txt");
            EcoPlateService service = new EcoPlateService(store);
            service.load();
            new MainFrame(service).setVisible(true);
        });
    }
}
