package com.example.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import com.example.finalproject.R;
import com.example.finalproject.database.DatabaseHelper;
import com.example.finalproject.model.Task;
import com.example.finalproject.utils.PriorityCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Calendar deadlineCalendar;
    private long finalDeadlineTimestamp = 0;

    private TextInputEditText inputTitle, inputDescription;
    private Spinner spinnerImportance;
    private Spinner spinnerCategory;
    private TextView tvSelectedDeadline;
    private Button btnSelectDeadline, btnSaveTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        dbHelper = new DatabaseHelper(this);
        deadlineCalendar = Calendar.getInstance();

        //connect var with id in xml
        inputTitle = findViewById(R.id.input_title);
        inputDescription = findViewById(R.id.input_description);
        spinnerImportance = findViewById(R.id.spinner_importance);
        spinnerCategory = findViewById(R.id.spinner_category);
        tvSelectedDeadline = findViewById(R.id.tv_selected_deadline);
        btnSelectDeadline = findViewById(R.id.btn_select_deadline);
        btnSaveTask = findViewById(R.id.btn_save_task);

        //listener
        btnSelectDeadline.setOnClickListener(v -> showDateTimePicker());

        btnSaveTask.setOnClickListener(v -> saveTask());
    }

    //method for show datepicker
    private void showDateTimePicker() {
        //show datepicker
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            deadlineCalendar.set(Calendar.YEAR, year);
            deadlineCalendar.set(Calendar.MONTH, month);
            deadlineCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                deadlineCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                deadlineCalendar.set(Calendar.MINUTE, minute);

                finalDeadlineTimestamp = deadlineCalendar.getTimeInMillis();

                updateDeadlineLabel();

            }, deadlineCalendar.get(Calendar.HOUR_OF_DAY), deadlineCalendar.get(Calendar.MINUTE), true).show();

        }, deadlineCalendar.get(Calendar.YEAR), deadlineCalendar.get(Calendar.MONTH), deadlineCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //method for change date to readable text
    private void updateDeadlineLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy, HH:mm", new Locale("id", "ID"));
        tvSelectedDeadline.setText("Deadline: " + sdf.format(deadlineCalendar.getTime()));
    }

    //main method
    private void saveTask() {
        //take data from input
        String title = inputTitle.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();
        int importanceLevel = spinnerImportance.getSelectedItemPosition() + 1;

        //take category
        String category = spinnerCategory.getSelectedItem().toString();

        //validate input
        if (title.isEmpty()) {
            inputTitle.setError("Judul wajib diisi!");
            return;
        }
        if (finalDeadlineTimestamp == 0) {
            Toast.makeText(this, "Mohon pilih tanggal deadline!", Toast.LENGTH_SHORT).show();
            return;
        }

        Task newTask = Task.createNewTask(
                title,
                description,
                finalDeadlineTimestamp,
                importanceLevel,
                category
        );

        //calculate priority score
        double calculatedScore = PriorityCalculator.calculateScore(newTask);
        newTask.setPriorityScore(calculatedScore);

        //save to db
        long result = dbHelper.addTask(newTask);

        if (result != -1) {
            //set alarm here
            scheduleNotification(finalDeadlineTimestamp, title, (int) result);
            Toast.makeText(this, "Task Saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to Save Task!", Toast.LENGTH_SHORT).show();
        }
    }

    //method for set alarm
    private void scheduleNotification(long timeInMillis, String taskTitle, int taskId) {
        if (timeInMillis <= System.currentTimeMillis()) {
            return;
        }

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);

        //provide intent for NotificationReceiver
        android.content.Intent intent = new android.content.Intent(this, com.example.finalproject.utils.NotificationReceiver.class);
        intent.putExtra("title", taskTitle);
        intent.putExtra("id", taskId);

        //wrap intent with pendingIntent
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(
                this,
                taskId,
                intent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
        );

        //set alarm
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    android.app.AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
            );
        }
    }
}