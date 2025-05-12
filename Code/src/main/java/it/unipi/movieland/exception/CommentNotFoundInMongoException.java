package it.unipi.movieland.exception;

public class CommentNotFoundInMongoException extends RuntimeException {
    public CommentNotFoundInMongoException(String message) {
        super(message);
    }
}