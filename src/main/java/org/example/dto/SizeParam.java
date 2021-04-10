package org.example.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class SizeParam {
    @NotNull
    @Digits(integer = 4, fraction = 0)
    private Integer numberOfPages;

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
