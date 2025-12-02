package com.example.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// Import komponen Material Design (untuk input text)
import com.google.android.material.textfield.TextInputEditText;

// Import class buatan kita
import com.example.finalproject.R;
import com.example.finalproject.database.DatabaseHelper;
import com.example.finalproject.model.Task;
import com.example.finalproject.utils.PriorityCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {

    // Variabel untuk Database dan Logic Waktu
    private DatabaseHelper dbHelper;
    private Calendar deadlineCalendar;
    private long finalDeadlineTimestamp = 0; // Default 0 (belum dipilih)

    // Deklarasi View (Komponen UI)
    private TextInputEditText inputTitle, inputDescription;
    private Spinner spinnerImportance;
    private TextView tvSelectedDeadline;
    private Button btnSelectDeadline, btnSaveTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // 1. Inisialisasi
        dbHelper = new DatabaseHelper(this);
        deadlineCalendar = Calendar.getInstance(); // Ambil waktu sekarang sebagai default

        // 2. Binding Views (Hubungkan variable dengan ID di XML)
        inputTitle = findViewById(R.id.input_title);
        inputDescription = findViewById(R.id.input_description);
        spinnerImportance = findViewById(R.id.spinner_importance);
        tvSelectedDeadline = findViewById(R.id.tv_selected_deadline);
        btnSelectDeadline = findViewById(R.id.btn_select_deadline);
        btnSaveTask = findViewById(R.id.btn_save_task);

        // 3. Pasang Listener (Aksi Klik)

        // Klik tombol pilih deadline -> Munculkan Date Picker
        btnSelectDeadline.setOnClickListener(v -> showDateTimePicker());

        // Klik tombol simpan -> Jalankan proses penyimpanan
        btnSaveTask.setOnClickListener(v -> saveTask());
    }

    // Method untuk menampilkan DatePicker lalu TimePicker berurutan
    private void showDateTimePicker() {
        // A. Tampilkan Date Picker
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            deadlineCalendar.set(Calendar.YEAR, year);
            deadlineCalendar.set(Calendar.MONTH, month);
            deadlineCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // B. Setelah tanggal dipilih, langsung Tampilkan Time Picker
            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                deadlineCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                deadlineCalendar.set(Calendar.MINUTE, minute);

                // Simpan hasil pilihan ke variabel timestamp
                finalDeadlineTimestamp = deadlineCalendar.getTimeInMillis();

                // Update teks di layar agar user tahu apa yang dipilih
                updateDeadlineLabel();

            }, deadlineCalendar.get(Calendar.HOUR_OF_DAY), deadlineCalendar.get(Calendar.MINUTE), true).show();

        }, deadlineCalendar.get(Calendar.YEAR), deadlineCalendar.get(Calendar.MONTH), deadlineCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Method untuk mengubah format tanggal jadi teks yang mudah dibaca (Contoh: Senin, 12 Des 2025, 14:00)
    private void updateDeadlineLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy, HH:mm", new Locale("id", "ID"));
        tvSelectedDeadline.setText("Deadline: " + sdf.format(deadlineCalendar.getTime()));
    }

    // Method Utama untuk Menyimpan Tugas
    private void saveTask() {
        // A. Ambil data dari inputan
        String title = inputTitle.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();

        // Ambil posisi spinner (0-4), tambah 1 biar jadi level (1-5)
        int importanceIndex = spinnerImportance.getSelectedItemPosition();
        int importanceLevel = importanceIndex + 1;

        // B. Validasi Input (Cegah data kosong)
        if (title.isEmpty()) {
            inputTitle.setError("Judul wajib diisi!");
            return;
        }
        if (finalDeadlineTimestamp == 0) {
            Toast.makeText(this, "Mohon pilih tanggal deadline!", Toast.LENGTH_SHORT).show();
            return;
        }

        // C. Buat Objek Task Baru (Pakai Static Factory Method yang sudah kita buat)
        Task newTask = Task.createNewTask(
                title,
                description,
                finalDeadlineTimestamp,
                importanceLevel
        );

        // D. HITUNG SKOR PRIORITAS (Inti dari aplikasi Smart To-Do List)
        double calculatedScore = PriorityCalculator.calculateScore(newTask);
        newTask.setPriorityScore(calculatedScore); // Masukkan skor ke objek task

        // E. Simpan ke Database
        long result = dbHelper.addTask(newTask);

        if (result != -1) {
            Toast.makeText(this, "Tugas berhasil disimpan!", Toast.LENGTH_SHORT).show();
            finish(); // Tutup halaman ini dan kembali ke MainActivity
        } else {
            Toast.makeText(this, "Gagal menyimpan tugas.", Toast.LENGTH_SHORT).show();
        }
    }
}