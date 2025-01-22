package it.unipi.movieland.model.Review;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Document(collection = "Reviews")

public class Review {
    @Id
    private String id;
    private String review;
    private String sentiment;
    private String movieId;
    private String username;
    private LocalDateTime timestamp;
    private int numLikes;

    // Costruttore vuoto (necessario per la deserializzazione)
    public Review() {
    }

    // Getter e Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", review='" + review + '\'' +
                ", sentiment=" + sentiment +
                ", movieId=" + movieId +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                ", numLikes='" + numLikes + '\'' +
                '}';
    }
}