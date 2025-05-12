package it.unipi.movieland.model.Review;

import lombok.Generated;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Document(collection = "Reviews")
public class ReviewMongoDB {

    @Id
    @Field("_id")
    @Generated
    private ObjectId id;

    private String review;
    private boolean sentiment;

    @Field("movie_id")
    private String movieId;

    private String username;
    private LocalDateTime timestamp;

    @Field("num_likes")
    private int numLikes;

    public String getStringId() {return id != null ? id.toString() : null; }

    public ReviewMongoDB(String review, boolean sentiment, String movieId, String username) {
        this.review = review;
        this.sentiment = sentiment;
        this.movieId = movieId;
        this.username = username;
        this.timestamp = LocalDateTime.now();
        this.numLikes = 0;
    }

    //GETTERS AND SETTERS
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public boolean isSentiment() { return sentiment; }
    public void setSentiment(boolean sentiment) { this.sentiment = sentiment; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getNumLikes() { return numLikes; }
    public void setNumLikes(int numLikes) { this.numLikes = numLikes; }

    @Override
    public String toString() {
        return "ReviewMongoDB{" +
                "id= " + id + '\'' +
                ", review= " + review + '\'' +
                ", sentiment= " + sentiment +
                ", movieId= " + movieId + '\'' +
                ", username= " + username + '\'' +
                ", timestamp= " + timestamp +
                ", numLikes= " + numLikes +
                '}';
    }
}