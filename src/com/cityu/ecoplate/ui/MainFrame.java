package com.cityu.ecoplate.ui;

import com.cityu.ecoplate.model.*;
import com.cityu.ecoplate.service.EcoPlateService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class MainFrame extends JFrame {
    private static final Color NAVY = new Color(20, 42, 67), GREEN = new Color(44, 150, 112), BG = new Color(244, 247, 246);
    private final EcoPlateService service;
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Batch", "Food", "Category", "Prepared kg", "Remaining kg", "Safe until", "Recommendation"}, 0) { public boolean isCellEditable(int r, int c) { return false; }};
    private final JTable table = new JTable(model);
    private final JLabel prepared = metricLabel(), remaining = metricLabel(), diverted = metricLabel(), co2 = metricLabel();
    public MainFrame(EcoPlateService service) {
        super("EcoPlate | SDG 12 Food Waste Simulator"); this.service = service;
        setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1180, 720); setMinimumSize(new Dimension(980, 620)); setLocationRelativeTo(null);
        setContentPane(build()); refresh();
    }
    private JPanel build() {
        JPanel root = new JPanel(new BorderLayout()); root.setBackground(BG);
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(NAVY); header.setBorder(new EmptyBorder(18, 26, 18, 26));
        JLabel title = new JLabel("EcoPlate"); title.setForeground(Color.WHITE); title.setFont(new Font("SansSerif", Font.BOLD, 27));
        JLabel sub = new JLabel("Smart Food Waste Reduction Simulator  •  UN SDG 12"); sub.setForeground(new Color(198, 224, 214)); sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPanel titles = new JPanel(new GridLayout(2,1)); titles.setOpaque(false); titles.add(title); titles.add(sub); header.add(titles, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout(16,16)); body.setBackground(BG); body.setBorder(new EmptyBorder(18, 22, 20, 22));
        JPanel metrics = new JPanel(new GridLayout(1,4,14,0)); metrics.setOpaque(false);
        metrics.add(metricCard("FOOD PREPARED", prepared)); metrics.add(metricCard("CURRENT SURPLUS", remaining)); metrics.add(metricCard("WASTE DIVERTED", diverted)); metrics.add(metricCard("CO₂ AVOIDED", co2)); body.add(metrics, BorderLayout.NORTH);
        table.setRowHeight(32); table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); table.getTableHeader().setBackground(new Color(226, 237, 232)); table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(table); scroll.setBorder(BorderFactory.createLineBorder(new Color(210,220,216))); body.add(scroll, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); actions.setOpaque(false);
        actions.add(button("+ Add food batch", e -> addBatch())); actions.add(button("Run demand simulation", e -> simulate())); actions.add(button("Apply recommendation", e -> applyAction())); actions.add(button("View impact summary", e -> showImpact()));
        body.add(actions, BorderLayout.SOUTH); root.add(body, BorderLayout.CENTER); return root;
    }
    private static JLabel metricLabel() { JLabel l = new JLabel("0", SwingConstants.LEFT); l.setFont(new Font("SansSerif", Font.BOLD, 25)); l.setForeground(NAVY); return l; }
    private JPanel metricCard(String name, JLabel value) { JPanel p = new JPanel(new BorderLayout(0,8)); p.setBackground(Color.WHITE); p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,228,225)), new EmptyBorder(14,16,14,16))); JLabel n = new JLabel(name); n.setForeground(new Color(91,107,102)); n.setFont(new Font("SansSerif", Font.BOLD, 11)); p.add(n, BorderLayout.NORTH); p.add(value, BorderLayout.CENTER); return p; }
    private JButton button(String text, java.awt.event.ActionListener listener) { JButton b = new JButton(text); b.setBackground(GREEN); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("SansSerif", Font.BOLD, 13)); b.addActionListener(listener); return b; }
    private void refresh() {
        model.setRowCount(0); DateTimeFormatter f = DateTimeFormatter.ofPattern("dd MMM, HH:mm");
        for (FoodBatch b : service.getBatches()) { FoodAction a = service.recommendationFor(b); model.addRow(new Object[]{b.getId(), b.getName(), b.getCategory(), fmt(b.getPreparedKg()), fmt(b.getRemainingKg()), b.getSafeUntil().format(f), a.getActionName()}); }
        prepared.setText(fmt(service.totalPrepared()) + " kg"); remaining.setText(fmt(service.totalRemaining()) + " kg"); diverted.setText(fmt(service.totalDiverted()) + " kg"); co2.setText(fmt(service.totalCo2Avoided()) + " kg");
    }
    private void addBatch() {
        JTextField name = new JTextField(), preparedKg = new JTextField(), remainKg = new JTextField(); JComboBox<FoodCategory> category = new JComboBox<>(FoodCategory.values()); JSpinner hours = new JSpinner(new SpinnerNumberModel(6,1,72,1));
        Object[] fields = {"Food name", name, "Category", category, "Prepared amount (kg)", preparedKg, "Current surplus (kg)", remainKg, "Safe for another (hours)", hours};
        if (JOptionPane.showConfirmDialog(this, fields, "Add food batch", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) try {
            service.addBatch(new FoodBatch("B-" + UUID.randomUUID().toString().substring(0,5).toUpperCase(), name.getText(), (FoodCategory) category.getSelectedItem(), Double.parseDouble(preparedKg.getText()), Double.parseDouble(remainKg.getText()), LocalDateTime.now().plusHours((Integer)hours.getValue()))); refresh();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Check the entered data", JOptionPane.WARNING_MESSAGE); }
    }
    private void simulate() { JSpinner customers = new JSpinner(new SpinnerNumberModel(30,1,500,1)); JComboBox<String> weather = new JComboBox<>(new String[]{"Normal","Rainy","Hot"}); Object[] fields = {"Expected customers per batch", customers, "Conditions", weather}; if (JOptionPane.showConfirmDialog(this, fields, "Demand simulation", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) { service.simulate((Integer) customers.getValue(), (String) weather.getSelectedItem()); refresh(); JOptionPane.showMessageDialog(this, "Simulation complete. Surplus and recommendations have been recalculated."); }}
    private void applyAction() { int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a food batch first."); return; } FoodBatch b = service.getBatches().get(row); FoodAction action = service.recommendationFor(b); if (JOptionPane.showConfirmDialog(this, action.explain() + "\n\nApply " + action.getActionName() + " to " + fmt(action.getEligibleKg()) + " kg?", "Decision support", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { ImpactRecord i = service.apply(action); refresh(); JOptionPane.showMessageDialog(this, String.format("Recorded %.1f kg diverted and %.1f kg CO₂ avoided.", i.divertedKg(), i.co2AvoidedKg())); }}
    private void showImpact() { double meals = service.getImpacts().stream().mapToDouble(ImpactRecord::mealsEquivalent).sum(); String message = String.format("CUMULATIVE IMPACT\n\nFood diverted: %.1f kg\nMeals supported: %.0f\nEstimated CO₂ avoided: %.1f kg\nCompleted actions: %d\n\nEcoPlate converts operational records into clear SDG 12 indicators.", service.totalDiverted(), meals, service.totalCo2Avoided(), service.getImpacts().size()); JOptionPane.showMessageDialog(this, message, "Impact summary", JOptionPane.INFORMATION_MESSAGE); }
    private static String fmt(double n) { return String.format("%.1f", n); }
}
