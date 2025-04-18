package com.example.slution;

import java.io.Serializable;
import java.util.Objects;

public class ChatItem implements Serializable {
    private String chatId;
    private String userName;
    private String lastMessage;
    private String time;

    public ChatItem() {
    }

    public ChatItem(String chatId, String userName, String lastMessage, String time) {
        this.chatId = chatId;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public ChatItem(String userName, String time) {
        this.userName = userName;
        this.time = time;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatItem)) return false;
        ChatItem item = (ChatItem) o;
        return Objects.equals(chatId, item.chatId) &&
                Objects.equals(userName, item.userName) &&
                Objects.equals(lastMessage, item.lastMessage) &&
                Objects.equals(time, item.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userName, lastMessage, time);
    }
}
