package it.unipi.movieland.exception;

public class MovieNotFoundInNeo4JException extends RuntimeException {
    public MovieNotFoundInNeo4JException(String message) {
        super(message);
    }
}
