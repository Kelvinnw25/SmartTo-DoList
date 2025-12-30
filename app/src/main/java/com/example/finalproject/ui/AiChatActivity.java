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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
        addBotMessage("Hello! Paste your task here, i will create you a reminder automatically.");

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

        // Ambil waktu sekarang di HP user
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        // 2. Siapkan Prompt Rahasia (Update Prompt)
        // Kita "selundupkan" info tanggal hari ini ke prompt
        String prompt = "Context: Current Date and Time is " + currentDateTime + ". " +
                "Extract info from this text: '" + userText + "'. " +
                "Return ONLY a JSON with fields: " +
                "title (string), description (string), deadline (string format 'yyyy-MM-dd HH:mm', calculate based on context), " +
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
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                                Log.e("GEMINI_ERROR", "Code: " + response.code() + " Message: " + errorBody);
                                addBotMessage("Gagal (" + response.code() + "): " + response.message());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
            // 1. Bersihin Markdown (biar JSON bersih)
            jsonText = jsonText.replace("```json", "").replace("```", "").trim();

            // 2. Convert JSON ke Object Java
            Gson gson = new Gson();
            TaskDraft draft = gson.fromJson(jsonText, TaskDraft.class);

            // 3. LOGIC BARU: Parsing Tanggal dari String ke Long
            long finalDeadline = 0;
            try {
                // Format ini harus SAMA PERSIS dengan request prompt kita ('yyyy-MM-dd HH:mm')
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
                if (draft.deadline != null && !draft.deadline.isEmpty()) {
                    java.util.Date date = sdf.parse(draft.deadline);
                    if (date != null) {
                        finalDeadline = date.getTime();
                    }
                }
            } catch (Exception e) {
                // Kalau AI gagal kasih format yg bener, default ke 24 jam dari sekarang
                finalDeadline = System.currentTimeMillis() + 86400000;
            }

            // 4. Simpan ke Database
            Task newTask = Task.createNewTask(
                    draft.title != null ? draft.title : "Task without title",
                    draft.description,
                    finalDeadline, // Pake deadline hasil parsing
                    draft.importance,
                    draft.category != null ? draft.category : "General"
            );

            dbHelper.addTask(newTask);

            addBotMessage("✅ Okay! Task '" + draft.title + "' saved!");

        } catch (Exception e) {
            Log.e("AI_ERROR", "Error parsing: " + e.getMessage());
            addBotMessage("Wow, I can't understand the text format. Please try again.");
        }
    }

    private void addBotMessage(String message) {
        chatList.add(new ChatMessage(message, false));
        chatAdapter.notifyItemInserted(chatList.size() - 1);
        rvChat.scrollToPosition(chatList.size() - 1);
    }
}