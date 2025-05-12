package it.unipi.movieland.exception;

public class ReviewNotFoundInNeo4JException extends RuntimeException {
    public ReviewNotFoundInNeo4JException(String message) {
        super(message);
    }
}
