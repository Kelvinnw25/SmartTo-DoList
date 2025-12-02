package com.example.finalproject.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    private final List<Task> taskList;
    private final Context context;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);

        //show data
        holder.tvTitle.setText(currentTask.getTitle());
        holder.cbCompleted.setChecked(currentTask.isCompleted());
        holder.tvScore.setText(String.format(Locale.getDefault(), "Skor: %.2f", currentTask.getPriorityScore()));

        //deadline format
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());
        String deadlineDate = sdf.format(currentTask.getDeadlineTimestamp());
        holder.tvDeadline.setText("Deadline: " + deadlineDate);

        //score color (visual)
        if (currentTask.getPriorityScore() > 80) { // Contoh: Prioritas Sangat Tinggi
            holder.tvScore.setTextColor(Color.RED);
        } else if (currentTask.getPriorityScore() > 40) { // Contoh: Prioritas Sedang
            holder.tvScore.setTextColor(Color.parseColor("#FFC107")); // Kuning
        } else {
            holder.tvScore.setTextColor(Color.GRAY);
        }

        //task done (visual)
        if (currentTask.isCompleted()) {
            holder.tvTitle.setTextColor(Color.GRAY);
        } else {
            holder.tvTitle.setTextColor(Color.BLACK);
        }

        //listener for checkbox
        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDeadline, tvScore;
        CheckBox cbCompleted;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvDeadline = itemView.findViewById(R.id.tv_task_deadline);
            tvScore = itemView.findViewById(R.id.tv_priority_score);
            cbCompleted = itemView.findViewById(R.id.cb_is_completed);
        }
    }

    //method for refresh data
    public void setTasks(List<Task> newTasks) {
        taskList.clear();
        taskList.addAll(newTasks);
        notifyDataSetChanged();
    }

}
