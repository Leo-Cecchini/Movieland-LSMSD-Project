package it.unipi.movieland.exception;

public class CelebrityNotFoundInNeo4jException extends RuntimeException {
    public CelebrityNotFoundInNeo4jException(String message) {
        super(message);
    }
}