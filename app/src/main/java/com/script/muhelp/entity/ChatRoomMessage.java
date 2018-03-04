package com.script.muhelp.entity;

import java.io.Serializable;
import java.util.Date;

public class ChatRoomMessage extends Position implements Serializable {
    private static final long serialVersionUID = 9;
    private User user;
    private Date date;
    private String content;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
