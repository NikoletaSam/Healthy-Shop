package com.example.healthyshop.model.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentCreationDto {

    @NotBlank
    @Size(min = 10)
    private String text;

    public CommentCreationDto() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
