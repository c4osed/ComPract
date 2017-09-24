package com.zoazh.le.ComPract.model.database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zatic Prasertarcha on 23/3/2560.
 */

public class Answer {

    public String Answer;
    public int Score;
    public String Comment;
    public long ASCAnswerTime;
    public long DESCAnswerTime;

    public Answer() {

    }

    public Answer(String answer, int score, String comment, long answerTimeASC, long answerTimeDESC) {
        this.Answer = answer;
        this.Score = score;
        this.Comment = comment;
        this.ASCAnswerTime = answerTimeASC;
        this.DESCAnswerTime = answerTimeDESC;
    }


    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("answer", Answer);
        result.put("score", Score);
        result.put("comment", Comment);
        result.put("answerTimeASC", ASCAnswerTime);
        result.put("answerTimeDESC", DESCAnswerTime);

        return result;
    }
}
