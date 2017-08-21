package com.zoazh.le.ComPract.model.database;

import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Zatic Prasertarcha on 11/3/2560.
 */

public class User {

    public String fullName;
    public String firstName;
    public String lastName;
    public String email;
    public String DOB;
    public String gender;
    public String country;
    public String nativeLanguage;
    public String learnFull;
    public String learnAbbreviation;
    public String profilePicture;
    public String about;
    public String onlineTime;
    public int followingCount;
    public int followerCount;
    public int answerCount;
    public int questionCount;
    public int advisorLevel;
    public int advisorEXP;
    public int studentLevel;
    public int studentEXP;


    public User() {

    }


    public User(String fullName, String firstName, String lastName, String email, String DOB, String gender, String country, String nativeLanguage, String learnFull, String learnAbbreviation, String profilePicture, String about,
                String onlineTime, int followingCount, int followerCount, int answerCount, int questionCount, int advisorLevel, int advisorEXP, int studentLevel, int studentEXP) {

        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.DOB = DOB;
        this.gender = gender;
        this.country = country;
        this.nativeLanguage = nativeLanguage;
        this.learnFull = learnFull;
        this.learnAbbreviation = learnAbbreviation;
        this.profilePicture = profilePicture;
        this.about = about;
        this.onlineTime = onlineTime;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.answerCount = answerCount;
        this.questionCount = questionCount;
        this.advisorLevel = advisorLevel;
        this.advisorEXP = advisorEXP;
        this.studentLevel = studentLevel;
        this.studentEXP = studentEXP;
    }


    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("fullName", fullName);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);
        result.put("DOB", DOB);
        result.put("gender", gender);
        result.put("country", country);
        result.put("nativeLanguage", nativeLanguage);
        result.put("learnFull",learnFull);
        result.put("learnAbbreviation",learnAbbreviation);
        result.put("profilePicture", profilePicture);
        result.put("about", about);
        result.put("onlineTime", onlineTime);
        result.put("followingCount", followingCount);
        result.put("followerCount", followerCount);
        result.put("answerCount", answerCount);
        result.put("questionCount", questionCount);
        result.put("advisorLevel", advisorLevel);
        result.put("advisorEXP", advisorEXP);
        result.put("studentLevel", studentLevel);
        result.put("studentEXP", studentEXP);

        return result;
    }
}
