package com.example.personalizedlearningexperienceapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PersonalizedLearningApp.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_USER_INTERESTS = "UserInterests";
    private static final String TABLE_QUIZ_RESULTS = "QuizResults";

    // User table columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";

    // User Interests table columns
    private static final String COLUMN_INTEREST_ID = "id";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_INTEREST = "interest";

    // Quiz Results table columns
    private static final String COLUMN_RESULT_ID = "id";
    private static final String COLUMN_QUIZ_TOPIC = "topic";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_COMPLETION_DATE = "completion_date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, "
                + COLUMN_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_FULL_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PHONE_NUMBER + " TEXT)";

        // Create UserInterests table
        String createInterestsTable = "CREATE TABLE " + TABLE_USER_INTERESTS + "("
                + COLUMN_INTEREST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_INTEREST + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        // Create QuizResults table
        String createQuizResultsTable = "CREATE TABLE " + TABLE_QUIZ_RESULTS + "("
                + COLUMN_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_QUIZ_TOPIC + " TEXT NOT NULL, "
                + COLUMN_SCORE + " INTEGER NOT NULL, "
                + COLUMN_COMPLETION_DATE + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createInterestsTable);
        db.execSQL(createQuizResultsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INTERESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // User management methods
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_FULL_NAME, user.getFullName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());

        long userId = db.insert(TABLE_USERS, null, values);

        // Add user interests if provided
        if (user.getInterests() != null && user.getInterests().length > 0) {
            for (String interest : user.getInterests()) {
                addUserInterest(userId, interest);
            }
        }

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

    // Quiz results methods
    public long saveQuizResult(long userId, String topic, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_QUIZ_TOPIC, topic);
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_COMPLETION_DATE, getCurrentDateTime());

        return db.insert(TABLE_QUIZ_RESULTS, null, values);
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

    private String getCurrentDateTime() {
        return String.valueOf(System.currentTimeMillis());
    }
}