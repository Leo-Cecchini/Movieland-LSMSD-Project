package it.unipi.movieland.exception;

public class MovieNotFoundInMongoException extends RuntimeException {
    public MovieNotFoundInMongoException(String message) {
        super(message);
    }
}
