package it.unipi.movieland.dto;

public class ReviewRatioDTO {
    private String id;
    private int positiveReviews;
    private int totalReviews;
    private int ratio;

    public String getId() {
        return id;
    }

    public int getPositiveReviews() {
        return positiveReviews;
    }

    public int getRatio() {
        return ratio;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPositiveReviews(int positiveReviews) {
        this.positiveReviews = positiveReviews;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public ReviewRatioDTO() {}
    public ReviewRatioDTO(String id, int positiveReviews, int totalReviews, int ratio) {
        this.id = id;
        this.positiveReviews = positiveReviews;
        this.totalReviews = totalReviews;
        this.ratio = ratio;
    }
}
