package com.example.personalizedlearningexperienceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.activity.QuestionActivity;

import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private final List<Map<String, String>> tasksList;
    private final long userId;
    private final String userName;

    public TaskAdapter(Context context, List<Map<String, String>> tasksList, long userId, String userName) {
        this.context = context;
        this.tasksList = tasksList;
        this.userId = userId;
        this.userName = userName;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Map<String, String> task = tasksList.get(position);

        holder.textViewTaskTitle.setText(task.get("title"));
        holder.textViewTaskDescription.setText(task.get("description"));

        // Set task topic if available
        if (holder.textViewTaskTopic != null && task.get("topic") != null) {
            holder.textViewTaskTopic.setText(task.get("topic"));
        }

        // Task click listener - navigate to QuestionActivity
        holder.itemView.setOnClickListener(v -> {
            String taskTitle = task.get("title");
            String taskDescription = task.get("description");
            String taskTopic = task.get("topic");

            Intent intent = new Intent(context, QuestionActivity.class);
            intent.putExtra("TASK_TITLE", taskTitle);
            intent.putExtra("TASK_DESCRIPTION", taskDescription);
            intent.putExtra("TASK_TOPIC", taskTopic);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USERNAME", userName);
            context.startActivity(intent);
        });

        // Delete button click listener
        holder.imageViewDelete.setOnClickListener(v -> {
            tasksList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tasksList.size());
        });

        // Star button click listener (for favorites)
        holder.imageViewStar.setOnClickListener(v -> {
            // Toggle star status - you can implement favorite functionality here
            // For now, just change the icon
            holder.imageViewStar.setSelected(!holder.imageViewStar.isSelected());
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskTitle;
        TextView textViewTaskDescription;
        TextView textViewTaskTopic;
        ImageView imageViewTaskStatus;
        ImageView imageViewDelete;
        ImageView imageViewStar;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewTaskDescription = itemView.findViewById(R.id.textViewTaskDescription);
            textViewTaskTopic = itemView.findViewById(R.id.textViewTaskTopic);
            imageViewTaskStatus = itemView.findViewById(R.id.imageViewTaskStatus);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            imageViewStar = itemView.findViewById(R.id.imageViewStar);
        }
    }
}