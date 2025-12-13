package com.example.finalproject.ui;

import android.content.Context;
import android.content.Intent;
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

        holder.tvTitle.setText(currentTask.getTitle());

        holder.cbCompleted.setOnCheckedChangeListener(null);
        holder.cbCompleted.setChecked(currentTask.isCompleted());

        //deadline format
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());
        String deadlineDate = sdf.format(currentTask.getDeadlineTimestamp());
        holder.tvDeadline.setText("Deadline: " + deadlineDate);

        //score view logic
        double score = currentTask.getPriorityScore();
        if (score > 1000000) { // Cek angka besar
            holder.tvScore.setText("OVERDUE!");
            holder.tvScore.setTextColor(Color.RED);
        } else {
            holder.tvScore.setText(String.format(Locale.getDefault(), "Skor: %.2f", score));

            //normal score color
            if (score > 80) holder.tvScore.setTextColor(Color.RED);
            else if (score > 40) holder.tvScore.setTextColor(Color.parseColor("#FFC107"));
            else holder.tvScore.setTextColor(Color.GRAY);
        }

        //visual if task is completed
        if (currentTask.isCompleted()) {
            holder.tvTitle.setTextColor(Color.GRAY);
        } else {
            holder.tvTitle.setTextColor(Color.BLACK);
        }

        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //update status
            currentTask.setCompleted(isChecked);

            //update visual
            if (isChecked) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            //update database
            com.example.finalproject.database.DatabaseHelper db = new com.example.finalproject.database.DatabaseHelper(context);
            db.updateTaskStatus(currentTask.getId(), isChecked);
            db.close();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTaskSelected.class);

            intent.putExtra("EXTRA_ID", currentTask.getId());
            intent.putExtra("EXTRA_TITLE", currentTask.getTitle());
            intent.putExtra("EXTRA_DESC", currentTask.getDescription());
            intent.putExtra("EXTRA_CATEGORY", currentTask.getCategory());
            intent.putExtra("EXTRA_DEADLINE", currentTask.getDeadlineTimestamp());
            intent.putExtra("EXTRA_IMPORTANCE", currentTask.getImportanceLevel());
            intent.putExtra("EXTRA_COMPLETED", currentTask.isCompleted());

            context.startActivity(intent);
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
