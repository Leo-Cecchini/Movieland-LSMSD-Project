package it.unipi.movieland.applicationMovieland.model.User;

public class RecentReview {
    private int reviewId;
    private String movieTitle;
    private String sentiment;
    private String content;

    public RecentReview(int reviewId, String movieTitle, String sentiment, String content) {
        this.reviewId = reviewId;
        this.movieTitle = movieTitle;
        this.sentiment = sentiment;
        this.content = content;
    }

    // Getter e Setter
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
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