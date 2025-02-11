package it.unipi.movieland.model.User;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class UserReview {
    @Field("review_id")
    private ObjectId review_id;
    private String movie_title;
    private boolean sentiment;
    private String content;

    public UserReview(ObjectId review_id, String movie_title, boolean sentiment, String content) {
        this.review_id = review_id;
        this.movie_title = movie_title;
        this.sentiment = sentiment;
        this.content = content;
    }

    @Override
    public String toString() {
        return "RecentReview{" +
                "reviewId=" + review_id +
                "movieTitle=" + movie_title +
                "sentiment=" + sentiment +
                "content=" + content +
                '}';
    }

    public ObjectId getReview_id() {
        return review_id;
    }

    public String getContent() {
        return content;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public boolean isSentiment() {
        return sentiment;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReview_id(ObjectId review_id) {
        this.review_id = review_id;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public void setSentiment(boolean sentiment) {
        this.sentiment = sentiment;
    }
}