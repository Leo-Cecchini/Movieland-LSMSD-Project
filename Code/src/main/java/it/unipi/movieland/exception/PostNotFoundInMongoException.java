package it.unipi.movieland.exception;


public class PostNotFoundInMongoException extends RuntimeException {
  public PostNotFoundInMongoException(String message) {
    super(message);
  }
}