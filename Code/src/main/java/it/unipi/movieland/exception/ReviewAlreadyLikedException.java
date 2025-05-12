package it.unipi.movieland.exception;

public class ReviewAlreadyLikedException extends RuntimeException {
    public ReviewAlreadyLikedException(String message) {
        super(message);
    }
}
