package com.example.personalizedlearningexperienceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question, position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        private TextView questionText;
        private RadioGroup radioGroup;
        private RadioButton option1, option2, option3, option4;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.textQuestion);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            option1 = itemView.findViewById(R.id.radio1);
            option2 = itemView.findViewById(R.id.radio2);
            option3 = itemView.findViewById(R.id.radio3);
            option4 = itemView.findViewById(R.id.radio4);
        }

        public void bind(Question question, int position) {
            questionText.setText((position + 1) + ". " + question.getQuestionText());

            List<String> options = question.getOptions();
            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));

            // Restore any previously selected answer
            radioGroup.clearCheck();
            if ("A".equals(question.getUserAnswer())) option1.setChecked(true);
            else if ("B".equals(question.getUserAnswer())) option2.setChecked(true);
            else if ("C".equals(question.getUserAnswer())) option3.setChecked(true);
            else if ("D".equals(question.getUserAnswer())) option4.setChecked(true);

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.radio1) {
                    question.setUserAnswer("A");
                } else if (checkedId == R.id.radio2) {
                    question.setUserAnswer("B");
                } else if (checkedId == R.id.radio3) {
                    question.setUserAnswer("C");
                } else if (checkedId == R.id.radio4) {
                    question.setUserAnswer("D");
                }
            });
        }
    }
}