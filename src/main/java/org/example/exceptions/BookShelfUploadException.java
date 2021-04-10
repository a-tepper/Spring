package org.example.exceptions;

public class BookShelfUploadException extends Exception {
    private String message;
    public BookShelfUploadException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
