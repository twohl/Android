package com.script.muhelp.entity;

import java.io.Serializable;
import java.util.Date;

public class Chat implements Serializable {
    private static final long serialVersionUID = 8;
    private int id;
    private Date date;
    private String content;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
