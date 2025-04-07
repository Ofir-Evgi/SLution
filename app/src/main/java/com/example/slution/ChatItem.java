package com.example.slution;

import java.io.Serializable;

public class ChatItem implements Serializable {
    private String userName;
    private String time;

    public ChatItem(String userName, String time) {
        this.userName = userName;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }
}
