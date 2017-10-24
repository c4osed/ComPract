package com.zoazh.le.ComPract.model.database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zatic Prasertarcha on 11/3/2560.
 */

public class Mission {

    public boolean missionAnswer;
    public boolean missionQuestion;
    public long missionTime;


    public Mission() {

    }


    public Mission(boolean missionAnswer, boolean missionQuestion, long missionTime) {

        this.missionAnswer = missionAnswer;
        this.missionQuestion = missionQuestion;
        this.missionTime = missionTime;
    }


    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("missionAnswer", missionAnswer);
        result.put("missionQuestion", missionQuestion);
        result.put("missionTime", missionTime);

        return result;
    }
}
