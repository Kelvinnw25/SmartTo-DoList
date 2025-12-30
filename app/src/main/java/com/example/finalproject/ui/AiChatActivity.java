package com.example.finalproject.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.api.GeminiApiService;
import com.example.finalproject.api.GeminiRequest;
import com.example.finalproject.api.GeminiResponse;
import com.example.finalproject.database.DatabaseHelper;
import com.example.finalproject.model.ChatMessage;
import com.example.finalproject.model.Task;
import com.example.finalproject.model.TaskDraft;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AiChatActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private EditText etInput;
    private Button btnSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatList;
    private GeminiApiService apiService;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        rvChat = findViewById(R.id.rvChat);
        etInput = findViewById(R.id.etInput);
        btnSend = findViewById(R.id.btnSend);

        // Setup Init
        dbHelper = new DatabaseHelper(this);
        apiService = GeminiApiService.create();
        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatList);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(chatAdapter);

        // Pesan Sapaan
        addBotMessage("Halo! Paste info tugasmu di sini, aku akan buatkan reminder otomatis.");

        btnSend.setOnClickListener(v -> {
            String text = etInput.getText().toString();
            if (!text.isEmpty()) {
                sendMessage(text);
            }
        });
    }

    private void sendMessage(String userText) {
        // 1. Tampilkan Chat User
        chatList.add(new ChatMessage(userText, true));
        chatAdapter.notifyItemInserted(chatList.size() - 1);
        rvChat.scrollToPosition(chatList.size() - 1);
        etInput.setText("");

        // 2. Siapkan Prompt Rahasia
        String prompt = "Extract info from this text: '" + userText + "'. " +
                "Return ONLY a JSON with fields: " +
                "title (string), description (string), deadline (string format 'yyyy-MM-dd HH:mm', if unsure use tomorrow), " +
                "importance (int 1-5), category (string). No markdown.";

        // 3. Panggil API
        apiService.chatWithGemini(GeminiApiService.API_KEY, new GeminiRequest(prompt))
                .enqueue(new Callback<GeminiResponse>() {
                    @Override
                    public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String rawResponse = response.body().getOutputText();
                            processAIResponse(rawResponse);
                        } else {
                            addBotMessage("Gagal konek ke AI :( Cek API Key.");
                        }
                    }

                    @Override
                    public void onFailure(Call<GeminiResponse> call, Throwable t) {
                        addBotMessage("Error: " + t.getMessage());
                    }
                });
    }

    private void processAIResponse(String jsonText) {
        try {
            // Bersihin Markdown kalau AI bandel kasih ```json
            jsonText = jsonText.replace("```json", "").replace("```", "").trim();

            // Convert JSON ke Object Java (TaskDraft)
            Gson gson = new Gson();
            TaskDraft draft = gson.fromJson(jsonText, TaskDraft.class);

            // Convert Date String ke Timestamp (Logic Simpel Dulu)
            // Di real project lu harus parse SimpleDateFormat
            long dummyTimestamp = System.currentTimeMillis() + 86400000; // Besok

            // Simpan ke Database
            Task newTask = Task.createNewTask(
                    draft.title != null ? draft.title : "Tugas Baru",
                    draft.description,
                    dummyTimestamp, // Nanti kita benerin logic parsing tanggalnya
                    draft.importance,
                    draft.category != null ? draft.category : "General"
            );

            dbHelper.addTask(newTask);

            addBotMessage("✅ Siap! Tugas '" + draft.title + "' berhasil disimpan!");

        } catch (Exception e) {
            Log.e("AI_ERROR", "Error parsing: " + e.getMessage());
            addBotMessage("Waduh, format teksnya susah dimengerti. Coba lagi ya.");
        }
    }

    private void addBotMessage(String message) {
        chatList.add(new ChatMessage(message, false));
        chatAdapter.notifyItemInserted(chatList.size() - 1);
        rvChat.scrollToPosition(chatList.size() - 1);
    }
}