package com.zoazh.le.ComPract.model.database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zatic Prasertarcha on 14/3/2560.
 */

public class Country {
    private String Abbreviation;
    private String Language;

    public Country() {
    }

    public Country(String abbreviation, String language) {
        Abbreviation = abbreviation;
        Language = language;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Abbreviation",Abbreviation);
        result.put("Language",Language);

        return result;
    }
}
