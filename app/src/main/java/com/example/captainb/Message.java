package com.example.captainb;

public class Message {
    public String userName;
    public String textMessage;
    private boolean isMine;

    Message(boolean isMine, String textMessage){
//        this.userName = userName;
        this.textMessage = textMessage;
        this.isMine = isMine;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
