package com.script.muhelp.entity;

import java.io.Serializable;

public class Note implements Serializable{
    private static final long serialVersionUID = 7;
    private int id;
    private User user;
    private String content;
    private User target;
    private NotWork notWork;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public NotWork getNotWork() {
        return notWork;
    }

    public void setNotWork(NotWork notWork) {
        this.notWork = notWork;
    }
}
