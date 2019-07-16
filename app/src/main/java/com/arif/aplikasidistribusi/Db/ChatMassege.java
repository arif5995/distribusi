package com.arif.aplikasidistribusi.Db;

import java.util.Date;

/**
 * Created by Angga Kristiono on 19/06/2019.
 */

public class ChatMassege {
    private String messageText, messageUser;
    private long messageTime;

    public ChatMassege(){

    }

    public ChatMassege(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = new Date().getTime();
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
