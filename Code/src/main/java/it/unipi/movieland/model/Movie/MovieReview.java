package it.unipi.movieland.model.Movie;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

public class MovieReview {

    @Id
    private ObjectId id;
    private boolean sentiment;
    private String username;
    private LocalDateTime timestamp;

    public MovieReview() {}

    public MovieReview(ObjectId id, boolean sentiment, String username, LocalDateTime timestamp) {
        this.id = id;
        this.sentiment = sentiment;
        this.username = username;
        this.timestamp = timestamp;
    }

    //GETTERS AND SETTERS
    public String getId() { return this.id.toHexString(); }
    public void setId(ObjectId id) { this.id = id; }

    public boolean isSentiment() { return sentiment; }
    public void setSentiment(boolean sentiment) { this.sentiment = sentiment; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "MovieReview{" +
                "id= " + id.toHexString() +
                ", sentiment= " + sentiment +
                ", username= " + username + '\'' +
                ", timestamp= " + timestamp +
                '}';
    }
}
