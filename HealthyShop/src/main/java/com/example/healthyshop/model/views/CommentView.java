package com.example.healthyshop.model.views;

import java.time.LocalDateTime;

public class CommentView {
    private String authorFullName;
    private String text;
    private LocalDateTime created;

    public CommentView() {
    }

    public CommentView(String authorFullName, String text, LocalDateTime created) {
        this.authorFullName = authorFullName;
        this.text = text;
        this.created = created;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreated(){
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
