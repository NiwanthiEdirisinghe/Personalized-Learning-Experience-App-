package com.example.personalizedlearningexperienceapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private RecyclerView recyclerViewTasks;
    private DBHelper dbHelper;
    private long userId;
    private String username;
    private String[] userInterests;

    private final Map<String, String> topicDescriptions = new HashMap<String, String>() {{
        put("Algorithms", "Design, analyze, and implement efficient algorithms for solving problems like searching, sorting, graph traversal, etc.");
        put("Data Structures", "Study and implement fundamental data structures like arrays, linked lists, stacks, queues, trees, and graphs.");
        put("Web Development", "Build websites or web applications using front-end technologies (HTML, CSS, JavaScript) and back-end frameworks (Node.js, Django, etc.).");
        put("Testing", "Implement and execute various types of tests (unit tests, integration tests, performance tests) to ensure software reliability.");
        put("Mobile Development", "Design and develop mobile applications for Android or iOS platforms, using Java, Kotlin, Swift, or React Native.");
        put("Database Design", "Design and implement relational or NoSQL databases, focusing on normalization, indexing, and query optimization.");
        put("Software Architecture", "Design and manage the overall software structure, ensuring scalability, maintainability, and efficiency of software systems.");
        put("UI/UX Design", "Create user interfaces and user experiences that are intuitive, responsive, and accessible for software applications.");
        put("Machine Learning", "Develop models that can analyze and predict patterns from data using algorithms like regression, classification, and clustering.");
        put("Cloud Computing", "Design and deploy cloud-based solutions using services from providers like AWS, Google Cloud, or Azure, ensuring scalability and security.");
        put("DevOps", "Integrate development and operations to automate the deployment pipeline, improve efficiency, and maintain system reliability.");
        put("Security", "Implement security measures to protect software systems from unauthorized access, attacks, and vulnerabilities.");
        put("Python", "Develop applications and scripts using Python for various purposes, including web development, automation, data analysis, and more.");
        put("Java", "Design and build applications using Java, focusing on object-oriented programming, concurrency, and large-scale system design.");
        put("JavaScript", "Build dynamic and interactive web applications using JavaScript and frameworks like React, Angular, or Vue.js.");
        put("Blockchain", "Develop decentralized applications and smart contracts using blockchain technology, focusing on security and distributed consensus.");
        put("IoT Development", "Build systems that connect physical devices to the internet, enabling data collection, monitoring, and control of these devices remotely.");
        put("Game Development", "Create interactive games for various platforms using game engines like Unity or Unreal, focusing on graphics, physics, and user engagement.");
        put("Big Data", "Process and analyze large volumes of data using frameworks like Hadoop, Spark, or Flink to extract meaningful insights and patterns.");
        put("Network Programming", "Develop applications that communicate over networks, implementing protocols and ensuring reliable data transmission.");
        put("Embedded Systems", "Design and program hardware devices with microcontrollers, focusing on real-time operations and resource constraints.");
        put("Natural Language Processing", "Build systems that can understand, interpret, and generate human language, enabling human-computer interaction.");
        put("Computer Vision", "Develop algorithms that enable computers to interpret and understand visual information from the world, such as image recognition.");
        put("Deep Learning", "Implement neural networks with multiple layers to solve complex problems in areas like image recognition, speech recognition, and more.");
        put("Cybersecurity", "Identify and fix vulnerabilities in systems, implement secure coding practices, and develop countermeasures against cyber threats.");
        put("AR/VR Development", "Create immersive augmented reality or virtual reality experiences, focusing on 3D modeling, interaction design, and performance.");
        put("Functional Programming", "Develop software using functional programming paradigms with languages like Haskell, Scala, or functional features in mainstream languages.");
        put("Microservices", "Design and implement distributed systems composed of small, independent services that communicate over a network.");
        put("Docker & Kubernetes", "Containerize applications and orchestrate their deployment, scaling, and management across clusters of hosts.");
        put("Design Patterns", "Apply proven solutions to common software design problems, improving code quality, maintainability, and reusability.");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userId = getIntent().getLongExtra("USER_ID", -1);
        username = getIntent().getStringExtra("USERNAME");

        dbHelper = new DBHelper(this);

        userInterests = dbHelper.getUserInterests(userId);

        welcomeTextView = findViewById(R.id.textViewWelcome);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        welcomeTextView.setText("Hello,\n" + username);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        loadPersonalizedTasks();
    }

    private void loadPersonalizedTasks() {

        List<Map<String, String>> tasksList = new ArrayList<>();


        if (userInterests != null && userInterests.length > 0) {
            int taskNo = 1;
            for (String interest : userInterests) {
                Map<String, String> task = new HashMap<>();
                task.put("title", "Generated Task "+taskNo++);
                task.put("description", topicDescriptions.get(interest));
                task.put("topic", interest);
                tasksList.add(task);
            }
        }

        if (tasksList.isEmpty()) {
            String[] defaultTopics = {"Algorithms", "Data Structures", "Web Development", "Testing"};
            for (String topic : defaultTopics) {
                Map<String, String> task = new HashMap<>();
                task.put("title", "Generated Task 1");
                task.put("description", "Small Description for the generated Task");
                task.put("topic", topic);
                tasksList.add(task);
            }
        }

        TaskAdapter adapter = new TaskAdapter(this, tasksList, userId, username);
        recyclerViewTasks.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPersonalizedTasks();
    }
}