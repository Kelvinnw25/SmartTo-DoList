package com.example.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailTaskSelected extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int taskId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_task_selected);

        dbHelper = new DatabaseHelper(this);

        //back button initialization
        android.widget.ImageButton btnBack = findViewById(R.id.btnBack);
        //logic click
        btnBack.setOnClickListener(v -> {
            finish();
        });

        //init views
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDeadline = findViewById(R.id.tvDetailDeadline);
        TextView tvPriority = findViewById(R.id.tvDetailPriority);
        Spinner spinnerStatus = findViewById(R.id.spinnerStatus);

        //take data from intent
        taskId = getIntent().getIntExtra("EXTRA_ID", -1);
        String title = getIntent().getStringExtra("EXTRA_TITLE");
        String desc = getIntent().getStringExtra("EXTRA_DESC");
        String category = getIntent().getStringExtra("EXTRA_CATEGORY");
        long deadlineTimestamp = getIntent().getLongExtra("EXTRA_DEADLINE", 0);
        int importance = getIntent().getIntExtra("EXTRA_IMPORTANCE", 1);
        boolean isCompleted = getIntent().getBooleanExtra("EXTRA_COMPLETED", false);

        //set text simple
        tvTitle.setText(title);
        tvDesc.setText(desc);

        //handle category null
        if(category == null) category = "-";
        tvCategory.setText("Category: " + category);

        //date format
        if (deadlineTimestamp != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm", new Locale("id", "ID"));
            String dateStr = sdf.format(new Date(deadlineTimestamp));
            tvDeadline.setText(dateStr);
        } else {
            tvDeadline.setText("-");
        }

        updatePriorityView(tvPriority, isCompleted, importance);

        //logic status
        if (isCompleted) {
            spinnerStatus.setSelection(1); //index 1 = Done
        } else {
            spinnerStatus.setSelection(0); //index 0 = Pending
        }

        //listener when user change status from spinner
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (taskId != -1) {
                    boolean newStatus = (position == 1);

                    // 1. Update Database
                    dbHelper.updateTaskStatus(taskId, newStatus);

                    // 2. [TAMBAHKAN INI] Update Tampilan Teks jadi DONE/Prioritas
                    updatePriorityView(tvPriority, newStatus, importance);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void updatePriorityView(TextView tvPriority, boolean isDone, int importance) {
        if (isDone) {
            tvPriority.setText("DONE");
            tvPriority.setTextColor(Color.parseColor("#388E3C")); // Hijau Mantap
        } else {
            switch (importance) {
                case 5:
                    tvPriority.setText("Urgent");
                    tvPriority.setTextColor(Color.parseColor("#B71C1C"));//dark red
                    break;
                case 4:
                    tvPriority.setText("High");
                    tvPriority.setTextColor(Color.RED);//light red
                    break;
                case 3:
                    tvPriority.setText("Medium");
                    tvPriority.setTextColor(Color.parseColor("#FF9800"));//orange
                    break;
                case 2:
                    tvPriority.setText("Low");
                    tvPriority.setTextColor(Color.parseColor("#4CAF50"));//standard green
                    break;
                case 1:
                    tvPriority.setText("Very Low");
                    tvPriority.setTextColor(Color.parseColor("#8BC34A"));//light green
                    break;
                default:
                    tvPriority.setText("No Priority");
                    tvPriority.setTextColor(Color.GRAY);
                    break;
            }
        }
    }
}