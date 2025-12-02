package com.example.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.finalproject.R;
import com.example.finalproject.database.DatabaseHelper;
import com.example.finalproject.model.Task;
import com.example.finalproject.utils.PriorityCalculator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        //connect variable with ID in XML)
        recyclerView = findViewById(R.id.recycler_view_tasks);
        FloatingActionButton fabAddTask = findViewById(R.id.fab_add_task);
        tvEmptyState = findViewById(R.id.tv_empty_state);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //onclicklistener
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        List<Task> taskList = dbHelper.getTasks();

        //recalculate
        taskList = recalculateScores(taskList);

        //check if the list is empty
        if (taskList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (taskAdapter == null) {
                taskAdapter = new TaskAdapter(this, taskList);
                recyclerView.setAdapter(taskAdapter);
            }
            else {
                taskAdapter.setTasks(taskList);
            }
        }
    }

    //additional logic to recalculate score and sort the list
    private List<Task> recalculateScores(List<Task> tasks) {
        for (Task task : tasks) {
            double newScore = PriorityCalculator.calculateScore(task);
            task.setPriorityScore(newScore);

            dbHelper.updateTaskScore(task.getId(), newScore);
        }

        //descending sort use comparator java for manual sort
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Double.compare(t2.getPriorityScore(), t1.getPriorityScore());
            }
        });

        return tasks;
    }
}