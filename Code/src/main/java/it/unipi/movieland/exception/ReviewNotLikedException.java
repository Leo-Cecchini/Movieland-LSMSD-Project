package it.unipi.movieland.exception;

public class ReviewNotLikedException extends RuntimeException {
  public ReviewNotLikedException(String message) {
    super(message);
  }
}
