package com.example.personalizedlearningexperienceapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiClient {
    private static final String TAG = "ApiClient";
    private static final String BASE_URL = "http://10.0.2.2:5000";

    public interface QuizCallback {
        void onQuizReceived(Quiz quiz);
        void onError(String errorMessage);
    }

    public static void getQuiz(String topic, final QuizCallback callback) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String topic = params[0];
                String urlStr = BASE_URL + "/getQuiz?topic=" + topic;

                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        return response.toString();
                    } else {
                        return "Error: " + responseCode;
                    }
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null && !result.startsWith("Error")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray quizArray = jsonObject.getJSONArray("quiz");

                        List<Question> questions = new ArrayList<>();

                        for (int i = 0; i < quizArray.length(); i++) {
                            JSONObject questionObj = quizArray.getJSONObject(i);

                            String questionText = questionObj.getString("question");
                            String correctAnswer = questionObj.getString("correct_answer");
                            JSONArray optionsArray = questionObj.getJSONArray("options");

                            List<String> options = new ArrayList<>();
                            for (int j = 0; j < optionsArray.length(); j++) {
                                options.add(optionsArray.getString(j));
                            }

                            Question question = new Question(questionText, options, correctAnswer);
                            questions.add(question);
                        }

                        Quiz quiz = new Quiz(topic, "Quiz about " + topic, questions);
                        callback.onQuizReceived(quiz);

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        callback.onError("Error parsing quiz data");
                    }
                } else {
                    Log.e(TAG, "API error: " + result);
                    callback.onError(result);
                }
            }
        }.execute(topic);
    }

    // For demo purposes, returns a dummy quiz
    public static Quiz getDummyQuiz(String topic) {
        List<Question> questions = new ArrayList<>();

        if (topic.equalsIgnoreCase("algorithms")) {
            questions.add(new Question(
                    "What data structure uses LIFO (Last In First Out)?",
                    Arrays.asList("Queue", "Stack", "LinkedList", "Tree"),
                    "B"
            ));
            questions.add(new Question(
                    "Which sorting algorithm has the best average-case performance?",
                    Arrays.asList("Bubble Sort", "Selection Sort", "Quick Sort", "Insertion Sort"),
                    "C"
            ));
            questions.add(new Question(
                    "What is the time complexity of binary search?",
                    Arrays.asList("O(n)", "O(n log n)", "O(log n)", "O(1)"),
                    "C"
            ));
        } else if (topic.equalsIgnoreCase("data structures")) {
            questions.add(new Question(
                    "Which data structure is non-linear?",
                    Arrays.asList("Array", "Stack", "Queue", "Tree"),
                    "D"
            ));
            questions.add(new Question(
                    "What is the worst-case time complexity for searching in a hash table?",
                    Arrays.asList("O(1)", "O(n)", "O(log n)", "O(n²)"),
                    "B"
            ));
            questions.add(new Question(
                    "Which data structure uses FIFO (First In First Out) principle?",
                    Arrays.asList("Stack", "Tree", "Queue", "Graph"),
                    "C"
            ));
        } else {
            // Default topics
            questions.add(new Question(
                    "What is the primary purpose of object-oriented programming?",
                    Arrays.asList("Code optimization", "Encapsulation and reusability", "Memory management", "Sequential execution"),
                    "B"
            ));
            questions.add(new Question(
                    "Which programming paradigm uses 'pure functions'?",
                    Arrays.asList("Object-oriented", "Procedural", "Functional", "Imperative"),
                    "C"
            ));
            questions.add(new Question(
                    "What does API stand for?",
                    Arrays.asList("Application Programming Interface", "Automated Programming Interface", "Application Process Integration", "Advanced Programming Implementation"),
                    "A"
            ));
        }

        return new Quiz(topic, "Quiz about " + topic, questions);
    }
}
