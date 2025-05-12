package it.unipi.movieland.exception;

public class UserNotFoundInMongoException extends RuntimeException {
    public UserNotFoundInMongoException(String message) {
        super(message);
    }
}
