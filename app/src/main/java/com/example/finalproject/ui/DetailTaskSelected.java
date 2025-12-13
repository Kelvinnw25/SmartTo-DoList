package com.example.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import com.example.finalproject.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailTaskSelected extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_task_selected);

        // 1. Init Views
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDeadline = findViewById(R.id.tvDetailDeadline);
        TextView tvPriority = findViewById(R.id.tvDetailPriority);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);

        // 2. Tangkap Data dari Intent
        String title = getIntent().getStringExtra("EXTRA_TITLE");
        String desc = getIntent().getStringExtra("EXTRA_DESC");
        String category = getIntent().getStringExtra("EXTRA_CATEGORY");
        long deadlineTimestamp = getIntent().getLongExtra("EXTRA_DEADLINE", 0);
        int importance = getIntent().getIntExtra("EXTRA_IMPORTANCE", 1);
        boolean isCompleted = getIntent().getBooleanExtra("EXTRA_COMPLETED", false);

        // 3. Set Text Simple
        tvTitle.setText(title);
        tvDesc.setText(desc);

        // Handle Category null
        if(category == null) category = "-";
        tvCategory.setText("Category: " + category);

        // 4. Format Tanggal (Timestamp -> Bacaan Manusia)
        if (deadlineTimestamp != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm", new Locale("id", "ID"));
            String dateStr = sdf.format(new Date(deadlineTimestamp));
            tvDeadline.setText(dateStr);
        } else {
            tvDeadline.setText("-");
        }

        // 5. Logic Text & Warna Prioritas
        switch (importance) {
            case 3: // High
                tvPriority.setText("High Priority");
                tvPriority.setTextColor(Color.RED);
                break;
            case 2: // Medium
                tvPriority.setText("Medium Priority");
                tvPriority.setTextColor(Color.parseColor("#FFA000")); // Orange
                break;
            default: // Low
                tvPriority.setText("Low Priority");
                tvPriority.setTextColor(Color.parseColor("#388E3C")); // Hijau
                break;
        }

        // 6. Logic Status
        if (isCompleted) {
            tvStatus.setText("✅ Done");
            tvStatus.setTextColor(Color.parseColor("#388E3C")); // Hijau
        } else {
            tvStatus.setText("⏳ Pending");
            tvStatus.setTextColor(Color.parseColor("#1976D2")); // Biru
        }
    }
}