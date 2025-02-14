package it.unipi.movieland.model.Movie;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

public class MovieReview {
    @Field("id")
    private ObjectId id;
    private boolean sentiment;
    private String username;
    private LocalDateTime timestamp;

    public String getId() {
        return this.id.toHexString();
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSentiment() {
        return sentiment;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSentiment(boolean sentiment) {
        this.sentiment = sentiment;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public MovieReview(ObjectId id, boolean sentiment, String username, LocalDateTime timestamp) {
        this.id = id;
        this.sentiment = sentiment;
        this.username = username;
        this.timestamp = timestamp;
    }

    public MovieReview() {}
}
