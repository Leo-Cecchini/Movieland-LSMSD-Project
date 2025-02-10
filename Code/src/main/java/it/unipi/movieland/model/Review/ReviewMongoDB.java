package it.unipi.movieland.model.Review;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Reviews")
public class ReviewMongoDB {
    @Id
    @Generated
    private String _id;
    private String review;
    private boolean sentiment;
    private String movie_id;
    private String username;
    private LocalDateTime timestamp;
    private int num_likes;

    public ReviewMongoDB(String review, boolean sentiment, String movie_id, String username) {
        this.review = review;
        this.sentiment = sentiment;
        this.movie_id = movie_id;
        this.username = username;
        this.timestamp = LocalDateTime.now();
        this.num_likes = 0;
    }

    public String getUsername() {
        return username;
    }

    public String get_id() {
        return _id;
    }

    public int getNum_likes() {
        return num_likes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public boolean isSentiment() {
        return sentiment;
    }

    public String getReview() {
        return review;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSentiment(boolean sentiment) {
        this.sentiment = sentiment;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}