package org.example.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TitleParam {
    @NotBlank
    @Size(min = 1, max = 50, message = "Title is empty or too long")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
