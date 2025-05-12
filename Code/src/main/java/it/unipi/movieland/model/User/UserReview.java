package it.unipi.movieland.model.User;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public class UserReview {

    @Field("review_id")
    private ObjectId reviewId;

    @Field("movie_title")
    private String movieTitle;

    private boolean sentiment;
    private String content;

    public UserReview(ObjectId reviewId, String movieTitle, boolean sentiment, String content) {
        this.reviewId = reviewId;
        this.movieTitle = movieTitle;
        this.sentiment = sentiment;
        this.content = content;
    }

    public ObjectId getReviewId() {
        return reviewId;
    }
    public void setReviewId(ObjectId reviewId) {
        this.reviewId = reviewId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public boolean isSentiment() {
        return sentiment;
    }
    public void setSentiment(boolean sentiment) {
        this.sentiment = sentiment;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RecentReview{" +
                "reviewId=" + reviewId +
                "movieTitle=" + movieTitle +
                "sentiment=" + sentiment +
                "content=" + content +
                '}';
    }
}