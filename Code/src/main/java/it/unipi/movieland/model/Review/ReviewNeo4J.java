package it.unipi.movieland.model.Review;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Review")
public class ReviewNeo4J {

    @Id
    @Property("review_id")
    private String reviewId;
    private boolean sentiment;

    public ReviewNeo4J(String reviewId, boolean sentiment) {
        this.reviewId = reviewId;
        this.sentiment = sentiment;
    }

    public String getReviewId() {
        return reviewId;
    }
    public void setReviewId(String review_id) {
        this.reviewId = review_id;
    }

    public boolean isSentiment() {
        return sentiment;
    }
    public void setSentiment(boolean sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "ReviewNeo4J{" +
                "review_id= " + reviewId + '\'' +
                ", sentiment= " + sentiment +
                '}';
    }
}