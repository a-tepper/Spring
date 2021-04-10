package org.example.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthorParam {
    @NotBlank
    @Size(min = 1, max = 50, message = "Author is empty or too long")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
