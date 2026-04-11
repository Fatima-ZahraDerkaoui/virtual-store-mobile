package com.virtualstore.virtualstore.Panier_Historique;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.HistoryAdapter;
import com.virtualstore.virtualstore.database.OrderDAO;
import com.virtualstore.virtualstore.model.HistoryItem;

import java.util.*;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // ✅ Toolbar FIX
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Historique");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recycler = findViewById(R.id.recyclerHistory);
        OrderDAO db = new OrderDAO(this);

        List<HistoryItem> list = new ArrayList<>();

        Cursor c = db.getAllOrders();

        if (c != null && c.moveToFirst()) {
            do {
                list.add(new HistoryItem(
                        c.getInt(c.getColumnIndexOrThrow("order_id")),
                        c.getString(c.getColumnIndexOrThrow("date")),
                        c.getDouble(c.getColumnIndexOrThrow("total"))
                ));
            } while (c.moveToNext());

            c.close();
        }

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new HistoryAdapter(list, this));
    }

    // ✅ bouton retour
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}