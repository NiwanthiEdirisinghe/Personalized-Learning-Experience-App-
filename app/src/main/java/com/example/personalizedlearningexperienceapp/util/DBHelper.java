package com.example.personalizedlearningexperienceapp.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.personalizedlearningexperienceapp.entity.QuestionResult;
import com.example.personalizedlearningexperienceapp.entity.QuizHistory;
import com.example.personalizedlearningexperienceapp.entity.SubscriptionPlan;
import com.example.personalizedlearningexperienceapp.entity.User;
import com.example.personalizedlearningexperienceapp.entity.UserStats;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PersonalizedLearningApp.db";
    private static final int DATABASE_VERSION = 3; // INCREMENT THIS TO 3

    // Tables
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_USER_INTERESTS = "UserInterests";
    private static final String TABLE_QUIZ_RESULTS = "QuizResults";
    private static final String TABLE_SUBSCRIPTION_PLANS = "SubscriptionPlans";
    private static final String TABLE_USER_ACHIEVEMENTS = "UserAchievements";
    private static final String TABLE_QUESTION_RESULTS = "QuestionResults"; // NEW

    // User table columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_SUBSCRIPTION_PLAN = "subscription_plan";
    private static final String COLUMN_SUBSCRIPTION_DATE = "subscription_date";
    private static final String COLUMN_JOIN_DATE = "join_date";
    private static final String COLUMN_PROFILE_PICTURE = "profile_picture";

    // User Interests table columns
    private static final String COLUMN_INTEREST_ID = "id";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_INTEREST = "interest";

    // Quiz Results table columns
    private static final String COLUMN_RESULT_ID = "id";
    private static final String COLUMN_QUIZ_TOPIC = "topic";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_TOTAL_QUESTIONS = "total_questions";
    private static final String COLUMN_COMPLETION_DATE = "completion_date";
    private static final String COLUMN_DIFFICULTY = "difficulty";

    // Question Results table columns (NEW)
    private static final String COLUMN_QUESTION_RESULT_ID = "id";
    private static final String COLUMN_QUIZ_RESULT_ID_FK = "quiz_result_id";
    private static final String COLUMN_QUESTION_NUMBER = "question_number";
    private static final String COLUMN_QUESTION_TEXT = "question_text";
    private static final String COLUMN_USER_ANSWER = "user_answer";
    private static final String COLUMN_CORRECT_ANSWER = "correct_answer";
    private static final String COLUMN_IS_CORRECT = "is_correct";

    // Subscription Plans table columns
    private static final String COLUMN_PLAN_ID = "id";
    private static final String COLUMN_PLAN_NAME = "plan_name";
    private static final String COLUMN_PLAN_PRICE = "price";
    private static final String COLUMN_PLAN_FEATURES = "features";
    private static final String COLUMN_PLAN_DURATION = "duration_days";

    // User Achievements table columns
    private static final String COLUMN_ACHIEVEMENT_ID = "id";
    private static final String COLUMN_ACHIEVEMENT_TYPE = "achievement_type";
    private static final String COLUMN_ACHIEVEMENT_TITLE = "title";
    private static final String COLUMN_ACHIEVEMENT_DESC = "description";
    private static final String COLUMN_EARNED_DATE = "earned_date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, "
                + COLUMN_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_FULL_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PHONE_NUMBER + " TEXT, "
                + COLUMN_SUBSCRIPTION_PLAN + " TEXT DEFAULT 'free', "
                + COLUMN_SUBSCRIPTION_DATE + " TEXT, "
                + COLUMN_JOIN_DATE + " TEXT DEFAULT '" + getCurrentDateTime() + "', "
                + COLUMN_PROFILE_PICTURE + " TEXT)";

        String createInterestsTable = "CREATE TABLE " + TABLE_USER_INTERESTS + "("
                + COLUMN_INTEREST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_INTEREST + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        String createQuizResultsTable = "CREATE TABLE " + TABLE_QUIZ_RESULTS + "("
                + COLUMN_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_QUIZ_TOPIC + " TEXT NOT NULL, "
                + COLUMN_SCORE + " INTEGER NOT NULL, "
                + COLUMN_TOTAL_QUESTIONS + " INTEGER DEFAULT 3, "
                + COLUMN_DIFFICULTY + " TEXT DEFAULT 'medium', "
                + COLUMN_COMPLETION_DATE + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        String createQuestionResultsTable = "CREATE TABLE " + TABLE_QUESTION_RESULTS + "("
                + COLUMN_QUESTION_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_QUIZ_RESULT_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_QUESTION_NUMBER + " INTEGER NOT NULL, "
                + COLUMN_QUESTION_TEXT + " TEXT NOT NULL, "
                + COLUMN_USER_ANSWER + " TEXT NOT NULL, "
                + COLUMN_CORRECT_ANSWER + " TEXT NOT NULL, "
                + COLUMN_IS_CORRECT + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_QUIZ_RESULT_ID_FK + ") REFERENCES "
                + TABLE_QUIZ_RESULTS + "(" + COLUMN_RESULT_ID + "))";

        String createSubscriptionPlansTable = "CREATE TABLE " + TABLE_SUBSCRIPTION_PLANS + "("
                + COLUMN_PLAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLAN_NAME + " TEXT NOT NULL, "
                + COLUMN_PLAN_PRICE + " REAL NOT NULL, "
                + COLUMN_PLAN_FEATURES + " TEXT, "
                + COLUMN_PLAN_DURATION + " INTEGER DEFAULT 30)";

        String createAchievementsTable = "CREATE TABLE " + TABLE_USER_ACHIEVEMENTS + "("
                + COLUMN_ACHIEVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_ACHIEVEMENT_TYPE + " TEXT NOT NULL, "
                + COLUMN_ACHIEVEMENT_TITLE + " TEXT NOT NULL, "
                + COLUMN_ACHIEVEMENT_DESC + " TEXT, "
                + COLUMN_EARNED_DATE + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createInterestsTable);
        db.execSQL(createQuizResultsTable);
        db.execSQL(createQuestionResultsTable);
        db.execSQL(createSubscriptionPlansTable);
        db.execSQL(createAchievementsTable);

        insertDefaultSubscriptionPlans(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_SUBSCRIPTION_PLAN + " TEXT DEFAULT 'free'");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_SUBSCRIPTION_DATE + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_QUIZ_RESULTS + " ADD COLUMN " + COLUMN_TOTAL_QUESTIONS + " INTEGER DEFAULT 3");
            } catch (Exception e) {
            }
        }

        if (oldVersion < 3) {
            // Add new columns for Task 10.1
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_JOIN_DATE + " TEXT DEFAULT '" + getCurrentDateTime() + "'");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_PROFILE_PICTURE + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_QUIZ_RESULTS + " ADD COLUMN " + COLUMN_DIFFICULTY + " TEXT DEFAULT 'medium'");
            } catch (Exception e) {

            }

            String createQuestionResultsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION_RESULTS + "("
                    + COLUMN_QUESTION_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_QUIZ_RESULT_ID_FK + " INTEGER NOT NULL, "
                    + COLUMN_QUESTION_NUMBER + " INTEGER NOT NULL, "
                    + COLUMN_QUESTION_TEXT + " TEXT NOT NULL, "
                    + COLUMN_USER_ANSWER + " TEXT NOT NULL, "
                    + COLUMN_CORRECT_ANSWER + " TEXT NOT NULL, "
                    + COLUMN_IS_CORRECT + " INTEGER NOT NULL, "
                    + "FOREIGN KEY(" + COLUMN_QUIZ_RESULT_ID_FK + ") REFERENCES "
                    + TABLE_QUIZ_RESULTS + "(" + COLUMN_RESULT_ID + "))";

            String createSubscriptionPlansTable = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBSCRIPTION_PLANS + "("
                    + COLUMN_PLAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PLAN_NAME + " TEXT NOT NULL, "
                    + COLUMN_PLAN_PRICE + " REAL NOT NULL, "
                    + COLUMN_PLAN_FEATURES + " TEXT, "
                    + COLUMN_PLAN_DURATION + " INTEGER DEFAULT 30)";

            String createAchievementsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_ACHIEVEMENTS + "("
                    + COLUMN_ACHIEVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                    + COLUMN_ACHIEVEMENT_TYPE + " TEXT NOT NULL, "
                    + COLUMN_ACHIEVEMENT_TITLE + " TEXT NOT NULL, "
                    + COLUMN_ACHIEVEMENT_DESC + " TEXT, "
                    + COLUMN_EARNED_DATE + " TEXT NOT NULL, "
                    + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

            db.execSQL(createQuestionResultsTable);
            db.execSQL(createSubscriptionPlansTable);
            db.execSQL(createAchievementsTable);

            insertDefaultSubscriptionPlans(db);
        }
    }


    public void createQuestionResultsTableIfNotExists() {
        SQLiteDatabase db = this.getWritableDatabase();
        String createQuestionResultsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION_RESULTS + "("
                + COLUMN_QUESTION_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_QUIZ_RESULT_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_QUESTION_NUMBER + " INTEGER NOT NULL, "
                + COLUMN_QUESTION_TEXT + " TEXT NOT NULL, "
                + COLUMN_USER_ANSWER + " TEXT NOT NULL, "
                + COLUMN_CORRECT_ANSWER + " TEXT NOT NULL, "
                + COLUMN_IS_CORRECT + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_QUIZ_RESULT_ID_FK + ") REFERENCES "
                + TABLE_QUIZ_RESULTS + "(" + COLUMN_RESULT_ID + "))";

        try {
            db.execSQL(createQuestionResultsTable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private void insertDefaultSubscriptionPlans(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_SUBSCRIPTION_PLANS, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return;
        }
        if (cursor != null) cursor.close();

        ContentValues starter = new ContentValues();
        starter.put(COLUMN_PLAN_NAME, "Starter");
        starter.put(COLUMN_PLAN_PRICE, 4.99);
        starter.put(COLUMN_PLAN_FEATURES, "• Unlimited quizzes\n• Basic analytics\n• Ad-free experience\n• Email support");
        starter.put(COLUMN_PLAN_DURATION, 30);
        db.insert(TABLE_SUBSCRIPTION_PLANS, null, starter);

        ContentValues intermediate = new ContentValues();
        intermediate.put(COLUMN_PLAN_NAME, "Intermediate");
        intermediate.put(COLUMN_PLAN_PRICE, 9.99);
        intermediate.put(COLUMN_PLAN_FEATURES, "• Everything in Starter\n• Detailed analytics\n• Custom topics\n• Progress tracking\n• Priority email support");
        intermediate.put(COLUMN_PLAN_DURATION, 30);
        db.insert(TABLE_SUBSCRIPTION_PLANS, null, intermediate);

        ContentValues advanced = new ContentValues();
        advanced.put(COLUMN_PLAN_NAME, "Advanced");
        advanced.put(COLUMN_PLAN_PRICE, 19.99);
        advanced.put(COLUMN_PLAN_FEATURES, "• Everything in Intermediate\n• 24/7 Priority support\n• Offline mode\n• Advanced insights\n• Personal tutor sessions");
        advanced.put(COLUMN_PLAN_DURATION, 30);
        db.insert(TABLE_SUBSCRIPTION_PLANS, null, advanced);
    }


    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_FULL_NAME, user.getFullName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_SUBSCRIPTION_PLAN, "free");
        values.put(COLUMN_JOIN_DATE, getCurrentDateTime());

        long userId = db.insert(TABLE_USERS, null, values);

        if (user.getInterests() != null && user.getInterests().length > 0) {
            for (String interest : user.getInterests()) {
                addUserInterest(userId, interest);
            }
        }
        addAchievement(userId, "welcome", "Welcome!", "Joined the learning community");

        return userId;
    }

    public long addUserInterest(long userId, String interest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_INTEREST, interest);

        return db.insert(TABLE_USER_INTERESTS, null, values);
    }

    public Cursor getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_USERS,
                null,
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null
        );
    }

    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null,
                null,
                null
        );

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int fullNameIndex = cursor.getColumnIndex(COLUMN_FULL_NAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int phoneNumberIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);

            long id = cursor.getLong(idIndex);
            String name = cursor.getString(usernameIndex);
            String fullName = cursor.getString(fullNameIndex);
            String email = cursor.getString(emailIndex);
            String phoneNumber = cursor.getString(phoneNumberIndex);

            user = new User(id, name, password, fullName, email, phoneNumber);
            user.setInterests(getUserInterests(id));
            cursor.close();
        }
        return user;
    }

    @SuppressLint("Range")
    public String[] getUserInterests(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USER_INTERESTS,
                new String[]{COLUMN_INTEREST},
                COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        String[] interests = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            interests[i++] = cursor.getString(cursor.getColumnIndex(COLUMN_INTEREST));
        }
        cursor.close();
        return interests;
    }

    public long saveQuizResultWithQuestions(long userId, String topic, int score, int totalQuestions,
                                            List<QuestionResult> questionResults) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues quizValues = new ContentValues();
        quizValues.put(COLUMN_USER_ID_FK, userId);
        quizValues.put(COLUMN_QUIZ_TOPIC, topic);
        quizValues.put(COLUMN_SCORE, score);
        quizValues.put(COLUMN_TOTAL_QUESTIONS, totalQuestions);
        quizValues.put(COLUMN_DIFFICULTY, "medium");
        quizValues.put(COLUMN_COMPLETION_DATE, getCurrentDateTime());

        long quizResultId = db.insert(TABLE_QUIZ_RESULTS, null, quizValues);

        if (quizResultId != -1 && questionResults != null) {
            for (QuestionResult result : questionResults) {
                ContentValues questionValues = new ContentValues();
                questionValues.put(COLUMN_QUIZ_RESULT_ID_FK, quizResultId);
                questionValues.put(COLUMN_QUESTION_NUMBER, result.getQuestionNumber());
                questionValues.put(COLUMN_QUESTION_TEXT, result.getQuestionText());
                questionValues.put(COLUMN_USER_ANSWER, result.getUserAnswer());
                questionValues.put(COLUMN_CORRECT_ANSWER, result.getCorrectAnswer());
                questionValues.put(COLUMN_IS_CORRECT, result.isCorrect() ? 1 : 0);

                db.insert(TABLE_QUESTION_RESULTS, null, questionValues);
            }
        }

        checkAndAwardAchievements(userId, score, totalQuestions);

        return quizResultId;
    }


    public long saveQuizResult(long userId, String topic, int score, int totalQuestions) {
        return saveQuizResultWithQuestions(userId, topic, score, totalQuestions, null);
    }

    public long saveQuizResult(long userId, String topic, int score) {
        return saveQuizResult(userId, topic, score, 3);
    }

    public Cursor getUserQuizResults(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_QUIZ_RESULTS,
                null,
                COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                COLUMN_COMPLETION_DATE + " DESC"
        );
    }

    @SuppressLint("Range")
    public List<QuizHistory> getQuizHistoryList(long userId) {
        List<QuizHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_QUIZ_RESULTS,
                null,
                COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                COLUMN_COMPLETION_DATE + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int quizResultId = cursor.getInt(cursor.getColumnIndex(COLUMN_RESULT_ID));
                String topic = cursor.getString(cursor.getColumnIndex(COLUMN_QUIZ_TOPIC));
                int score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETION_DATE));

                // Convert timestamp to readable date
                long timestamp = Long.parseLong(date);
                String formattedDate = java.text.DateFormat.getDateTimeInstance()
                        .format(new java.util.Date(timestamp));

                // Create QuizHistory with backward compatibility
                QuizHistory history = new QuizHistory(topic, score, formattedDate, true);

                // Get individual question results for this quiz
                List<QuestionResult> questionResults = getQuestionResultsForQuiz(quizResultId);
                history.setQuestionResults(questionResults);

                historyList.add(history);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return historyList;
    }


    @SuppressLint("Range")
    private List<QuestionResult> getQuestionResultsForQuiz(int quizResultId) {
        List<QuestionResult> questionResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_QUESTION_RESULTS,
                null,
                COLUMN_QUIZ_RESULT_ID_FK + "=?",
                new String[]{String.valueOf(quizResultId)},
                null,
                null,
                COLUMN_QUESTION_NUMBER + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int questionNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_QUESTION_NUMBER));
                String questionText = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_TEXT));
                String userAnswer = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ANSWER));
                String correctAnswer = cursor.getString(cursor.getColumnIndex(COLUMN_CORRECT_ANSWER));
                boolean isCorrect = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_CORRECT)) == 1;

                QuestionResult result = new QuestionResult(questionNumber, questionText,
                        userAnswer, correctAnswer, isCorrect);
                questionResults.add(result);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return questionResults;
    }

    @SuppressLint("Range")
    public UserStats getUserStats(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Get basic quiz stats
        Cursor cursor = db.query(
                TABLE_QUIZ_RESULTS,
                new String[]{"COUNT(*) as quiz_count", "SUM(" + COLUMN_SCORE + ") as total_score",
                        "SUM(" + COLUMN_TOTAL_QUESTIONS + ") as total_questions"},
                COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        int totalQuestions = 0;
        int correctAnswers = 0;

        if (cursor != null && cursor.moveToFirst()) {
            totalQuestions = cursor.getInt(cursor.getColumnIndex("total_questions"));
            correctAnswers = cursor.getInt(cursor.getColumnIndex("total_score"));
            cursor.close();
        }

        int incorrectAnswers = totalQuestions - correctAnswers;

        return new UserStats(totalQuestions, correctAnswers, incorrectAnswers);
    }

    @SuppressLint("Range")
    public List<SubscriptionPlan> getSubscriptionPlans() {
        List<SubscriptionPlan> plans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SUBSCRIPTION_PLANS, null, null, null, null, null, COLUMN_PLAN_PRICE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_PLAN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PLAN_PRICE));
                String features = cursor.getString(cursor.getColumnIndex(COLUMN_PLAN_FEATURES));

                SubscriptionPlan plan = new SubscriptionPlan(name, price, features);
                plans.add(plan);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return plans;
    }

    private void checkAndAwardAchievements(long userId, int score, int totalQuestions) {

        if (score == totalQuestions) {
            addAchievement(userId, "perfect_score", "Perfect Score!", "Got all questions correct in a quiz");
        }

        UserStats stats = getUserStats(userId);
        if (stats.getTotalQuestions() == 10 && !hasAchievement(userId, "first_10")) {
            addAchievement(userId, "first_10", "Getting Started", "Answered your first 10 questions");
        } else if (stats.getTotalQuestions() == 50 && !hasAchievement(userId, "half_century")) {
            addAchievement(userId, "half_century", "Half Century", "Answered 50 questions");
        } else if (stats.getTotalQuestions() == 100 && !hasAchievement(userId, "century")) {
            addAchievement(userId, "century", "Century Club", "Answered 100 questions");
        }
    }

    private void addAchievement(long userId, String type, String title, String description) {

        if (hasAchievement(userId, type)) return;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_ACHIEVEMENT_TYPE, type);
        values.put(COLUMN_ACHIEVEMENT_TITLE, title);
        values.put(COLUMN_ACHIEVEMENT_DESC, description);
        values.put(COLUMN_EARNED_DATE, getCurrentDateTime());

        db.insert(TABLE_USER_ACHIEVEMENTS, null, values);
    }

    private boolean hasAchievement(long userId, String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USER_ACHIEVEMENTS,
                null,
                COLUMN_USER_ID_FK + "=? AND " + COLUMN_ACHIEVEMENT_TYPE + "=?",
                new String[]{String.valueOf(userId), type},
                null,
                null,
                null
        );

        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        return exists;
    }

    public boolean updateUserSubscription(long userId, String planName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBSCRIPTION_PLAN, planName);
        values.put(COLUMN_SUBSCRIPTION_DATE, getCurrentDateTime());

        int rowsAffected = db.update(TABLE_USERS, values,
                COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});

        if (!planName.equals("free")) {
            addAchievement(userId, "premium_user", "Premium User", "Upgraded to a premium plan");
        }

        return rowsAffected > 0;
    }

    @SuppressLint("Range")
    public boolean hasPremiumSubscription(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_SUBSCRIPTION_PLAN},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        boolean hasPremium = false;
        if (cursor != null && cursor.moveToFirst()) {
            String plan = cursor.getString(cursor.getColumnIndex(COLUMN_SUBSCRIPTION_PLAN));
            hasPremium = plan != null && !plan.equals("free");
            cursor.close();
        }

        return hasPremium;
    }

    @SuppressLint("Range")
    public String getUserSubscriptionPlan(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_SUBSCRIPTION_PLAN},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        String plan = "free";
        if (cursor != null && cursor.moveToFirst()) {
            plan = cursor.getString(cursor.getColumnIndex(COLUMN_SUBSCRIPTION_PLAN));
            if (plan == null) plan = "free";
            cursor.close();
        }

        return plan;
    }

    private String getCurrentDateTime() {
        return String.valueOf(System.currentTimeMillis());
    }
}