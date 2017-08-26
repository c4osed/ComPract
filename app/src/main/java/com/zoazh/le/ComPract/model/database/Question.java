package com.zoazh.le.ComPract.model.database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zatic Prasertarcha on 25/8/2560.
 */

public class Question {


    public String QuestionAuthor;
    public String QuestionLanguage;
    public String QuestionType;
    public String QuestionPicture;
    public String Question;
    public String ChoiceA;
    public String ChoiceB;
    public String ChoiceC;
    public String ChoiceD;
    public String Answer;
    public String QuestionStatus;


    public Question() {

    }

    public Question(String questionAuthor, String questionLanguage, String questionType, String questionPicture, String question, String choiceA, String choiceB, String choiceC, String choiceD, String answer, String questionStatus) {
        QuestionAuthor = questionAuthor;
        QuestionLanguage = questionLanguage;
        QuestionType = questionType;
        QuestionPicture = questionPicture;
        Question = question;
        ChoiceA = choiceA;
        ChoiceB = choiceB;
        ChoiceC = choiceC;
        ChoiceD = choiceD;
        Answer = answer;
        QuestionStatus = questionStatus;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("QuestionAuthor", QuestionAuthor);
        result.put("QuestionLanguage", QuestionLanguage);
        result.put("QuestionType", QuestionType);
        result.put("QuestionPicture", QuestionPicture);
        result.put("Question", Question);
        result.put("ChoiceA", ChoiceA);
        result.put("ChoiceB", ChoiceB);
        result.put("ChoiceC", ChoiceC);
        result.put("ChoiceD", ChoiceD);
        result.put("Answer", Answer);
        result.put("QuestionStatus", QuestionStatus);

        return result;
    }
}
