package it.unipi.movieland.exception;

public class InvalidUpdateException extends RuntimeException {
    public InvalidUpdateException(String message) {
        super(message);
    }
}
