package com.example.personalizedlearningexperienceapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.entity.Question;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {

    private List<Question> questions;

    public ResultsAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.bind(question, position);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {

        private TextView textQuestionNumber;
        private TextView textResponse;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textQuestionNumber = itemView.findViewById(R.id.textQuestionNumber);
            textResponse = itemView.findViewById(R.id.textResponse);
        }

        public void bind(Question question, int position) {
            // Set question number
            textQuestionNumber.setText((position + 1) + ". " + question.getQuestionText());

            // Get the option text based on the user's answer
//            String userAnswerOption = "";
//            if ("A".equals(question.getUserAnswer())) {
//                userAnswerOption = question.getOptions().get(0);
//            } else if ("B".equals(question.getUserAnswer())) {
//                userAnswerOption = question.getOptions().get(1);
//            } else if ("C".equals(question.getUserAnswer())) {
//                userAnswerOption = question.getOptions().get(2);
//            } else if ("D".equals(question.getUserAnswer())) {
//                userAnswerOption = question.getOptions().get(3);
//            }
            String userAnswer = question.getUserAnswer();
            String correctAnswer = question.getCorrectAnswer();

            // Get answer text from options
            String userAnswerText = getOptionText(userAnswer, question.getOptions());
            String correctAnswerText = getOptionText(correctAnswer, question.getOptions());

            // Build dynamic response
            if (userAnswer.equals(correctAnswer)) {
                textResponse.setText("You selected " + userAnswer + ". Correct Answer! 🎉");
                itemView.setBackgroundColor(Color.parseColor("#C8E6C9")); // light green for correct
            } else {
                textResponse.setText("You selected " + userAnswer + ". Wrong Answer. Correct was " + correctAnswer + ".");
                itemView.setBackgroundColor(Color.parseColor("#FFCDD2")); // light red for wrong
            }

            // Set response text
            // Note: This could be customized to show more detailed feedback
           // textResponse.setText("Response from the model that you're using");

            // Set background color based on whether the answer is correct
            boolean isCorrect = question.getUserAnswer().equals(question.getCorrectAnswer());
            if (isCorrect) {
                itemView.setBackgroundColor(Color.parseColor("#1976D2")); // Correct answer color
            } else {
                itemView.setBackgroundColor(Color.parseColor("#1976D2")); // Using the same color as shown in image
            }
        }

        private String getOptionText(String optionLetter, List<String> options) {
            switch (optionLetter) {
                case "A":
                    return options.size() > 0 ? options.get(0) : "";
                case "B":
                    return options.size() > 1 ? options.get(1) : "";
                case "C":
                    return options.size() > 2 ? options.get(2) : "";
                case "D":
                    return options.size() > 3 ? options.get(3) : "";
                default:
                    return "";
            }
        }

    }
}