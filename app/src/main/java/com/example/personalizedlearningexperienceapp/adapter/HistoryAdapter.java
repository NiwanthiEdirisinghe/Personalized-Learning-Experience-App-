package com.example.personalizedlearningexperienceapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.entity.QuizHistory;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<QuizHistory> historyList;

    public HistoryAdapter(List<QuizHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        QuizHistory history = historyList.get(position);

        holder.questionNumber.setText(String.valueOf(position + 1));

        holder.questionTitle.setText(history.getQuestionTitle());

        String subtitle;
        if (history.hasDetailedResults()) {
            subtitle = String.format("Quiz attempt on %s • %s",
                    history.getDate(),
                    history.getAnswerBreakdown());
        } else {
            subtitle = "Quiz attempt on " + history.getDate();
        }
        holder.questionSubtitle.setText(subtitle);

        String answerInfo;
        if (history.hasDetailedResults()) {
            answerInfo = String.format("Score: %s (%s)",
                    history.getFormattedScore(),
                    history.getFormattedPercentage());
        } else {
            answerInfo = "Score: " + history.getScore();
        }
        holder.answerText.setText(answerInfo);

        boolean passed = history.isPassed();
        if (passed) {
            holder.statusIcon.setImageResource(R.drawable.ic_check_circle_green);
            holder.answerIcon.setImageResource(R.drawable.ic_check_small);
        } else {
            holder.statusIcon.setImageResource(R.drawable.ic_clock);
            holder.answerIcon.setImageResource(R.drawable.ic_x_small);
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumber, questionTitle, questionSubtitle, answerText;
        ImageView statusIcon, answerIcon;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.questionNumber);
            questionTitle = itemView.findViewById(R.id.questionTitle);
            questionSubtitle = itemView.findViewById(R.id.questionSubtitle);
            answerText = itemView.findViewById(R.id.answerText);
            statusIcon = itemView.findViewById(R.id.statusIcon);
            answerIcon = itemView.findViewById(R.id.answerIcon);
        }
    }

    public void updateHistoryList(List<QuizHistory> newHistoryList) {
        this.historyList = newHistoryList;
        notifyDataSetChanged();
    }
}