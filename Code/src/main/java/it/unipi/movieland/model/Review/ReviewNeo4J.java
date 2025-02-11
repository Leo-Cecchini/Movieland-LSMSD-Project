package it.unipi.movieland.model.Review;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Reviews")
public class ReviewNeo4J {
    @Id
    private String review_id;
    private boolean sentiment;

    public ReviewNeo4J(String review_id, boolean sentiment) {
        this.review_id = review_id;
        this.sentiment = sentiment;
    }

    public String getReview_id() {
        return review_id;
    }

    public boolean isSentiment() {
        return sentiment;
    }

    public void setSentiment(boolean sentiment) {
        this.sentiment = sentiment;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }
}
