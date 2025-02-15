package it.unipi.movieland.exception;

public class CelebrityNotFoundException extends RuntimeException {
    public CelebrityNotFoundException(String message) {
        super(message);
    }
}