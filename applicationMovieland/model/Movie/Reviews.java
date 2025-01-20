package applicationMovieland.model.Movie;

import java.util.List;

public class Reviews {
    private List<String> reviewList;

    public Reviews(List<String> reviewList) {
        this.reviewList = reviewList;
    }

    public List<String> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<String> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public String toString() {
        return "Reviews{" +
                "reviewList=" + reviewList +
                '}';
    }
}

