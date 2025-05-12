package it.unipi.movieland.exception;

public class ReviewNotFoundInMongoException extends RuntimeException {
    public ReviewNotFoundInMongoException(String message) {
        super(message);
    }
}
