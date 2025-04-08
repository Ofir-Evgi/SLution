package com.example.slution;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatItem)) return false;
        ChatItem item = (ChatItem) o;
        return userName.equals(item.userName) && time.equals(item.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, time);
    }
}
