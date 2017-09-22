package com.zoazh.le.ComPract.model.database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zatic Prasertarcha on 23/3/2560.
 */

public class Message {

    public String messageSender;
    public String messageText;
    public long messageTimeASC;
    public long messageTimeDESC;
    public String messageRead;

    public Message() {

    }

    public Message(String messageSender, String messageText, long messageTimeASC, long messageTimeDESC, String messageRead) {
        this.messageSender = messageSender;
        this.messageText = messageText;
        this.messageTimeASC = messageTimeASC;
        this.messageTimeDESC = messageTimeDESC;
        this.messageRead = messageRead;
    }


    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("messageSender", messageSender);
        result.put("messageText", messageText);
        result.put("messageTimeASC", messageTimeASC);
        result.put("messageTimeDESC", messageTimeDESC);
        result.put("messageRead", messageRead);

        return result;
    }
}
