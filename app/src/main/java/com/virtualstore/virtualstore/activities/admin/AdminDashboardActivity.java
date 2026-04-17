package com.virtualstore.virtualstore.activities.admin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.DataManager;
import com.virtualstore.virtualstore.model.Order;
import com.virtualstore.virtualstore.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboardActivity extends BaseAdminActivity {

    private PieChart pieChart;
    private Button btnExportPdf;
    private Button btnExportCsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin Dashboard");
        }

        pieChart = findViewById(R.id.pieChart);
        btnExportPdf = findViewById(R.id.btnExportPdf);
        btnExportCsv = findViewById(R.id.btnExportCsv);

        btnExportPdf.setOnClickListener(v -> exportToPdf());
        btnExportCsv.setOnClickListener(v -> exportToCsv());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
        setupPieChart();
    }

    private void updateStats() {
        TextView tvProducts = findViewById(R.id.tvProductsCount);
        TextView tvOrders = findViewById(R.id.tvOrdersCount);
        TextView tvUsers = findViewById(R.id.tvUsersCount);
        TextView tvRevenue = findViewById(R.id.tvRevenue);

        tvProducts.setText(String.valueOf(DataManager.products.size()));
        tvOrders.setText(String.valueOf(DataManager.orders.size()));
        tvUsers.setText(String.valueOf(DataManager.users.size()));

        double totalRevenue = 0;
        for (Order order : DataManager.orders) {
            totalRevenue += order.getTotalPrice();
        }
        tvRevenue.setText(totalRevenue + " DH");
    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();

        Map<String, Integer> categoryCounts = new HashMap<>();
        for (Product p : DataManager.products) {
            String cat = p.getCategory() != null ? p.getCategory() : "Uncategorized";
            categoryCounts.put(cat, categoryCounts.getOrDefault(cat, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Products");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void exportToCsv() {
        String fileName = "Rapport_Ventes.csv";
        StringBuilder csvData = new StringBuilder();
        csvData.append("ID Commande,Client,Montant (DH),Statut,Date\n");

        for (Order order : DataManager.orders) {
            csvData.append(order.getId()).append(",")
                    .append(order.getUserName()).append(",")
                    .append(order.getTotalPrice()).append(",")
                    .append(order.getStatus()).append(",")
                    .append(order.getDate()).append("\n");
        }

        try {
            File file = new File(getExternalFilesDir(null), fileName);
            FileOutputStream out = new FileOutputStream(file);
            out.write(csvData.toString().getBytes());
            out.close();

            Toast.makeText(this, "Rapport exporté : " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            shareFile(file, "text/csv");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'exportation", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportToPdf() {
        Toast.makeText(this, "Génération du PDF en cours...", Toast.LENGTH_SHORT).show();

        String fileName = "Rapport_Inventaire.txt";
        StringBuilder report = new StringBuilder();
        report.append("RAPPORT D'INVENTAIRE\n");
        report.append("====================\n\n");
        for (Product p : DataManager.products) {
            report.append("Produit: ").append(p.getName())
                    .append(" | Prix: ").append(p.getPrice())
                    .append(" DH | Stock: ").append(p.getStockQuantity()).append("\n");
        }

        try {
            File file = new File(getExternalFilesDir(null), fileName);
            FileOutputStream out = new FileOutputStream(file);
            out.write(report.toString().getBytes());
            out.close();
            shareFile(file, "text/plain");
        } catch (Exception e) {
            Toast.makeText(this, "Erreur PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareFile(File file, String mimeType) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Partager le rapport"));
    }
}