package it.unipi.movieland.exception;

public class CelebrityNotFoundInMongoException extends RuntimeException {
    public CelebrityNotFoundInMongoException(String message) {
        super(message);
    }
}