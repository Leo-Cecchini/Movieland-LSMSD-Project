package it.unipi.movieland.service.Review;

import it.unipi.movieland.model.Review.ReviewMongoDB;
import it.unipi.movieland.model.Review.ReviewNeo4J;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.repository.Review.ReviewMongoDBRepository;
import it.unipi.movieland.repository.Review.ReviewNeo4JRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewService {

    @Autowired
    private ReviewMongoDBRepository reviewMongoRepository;

    @Autowired
    private ReviewNeo4JRepository reviewNeo4JRepository;

    @Autowired
    private UserMongoDBRepository userMongoDBRepository;

    @Autowired
    private MovieMongoDBRepository movieMongoDBRepository;

    public ReviewMongoDB addReview(String movieId, String userId, String txt, boolean sentiment) {
        if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        } else if (!movieMongoDBRepository.existsById(movieId)) {
            throw new NoSuchElementException("Movie '" + movieId + "' doesn't exists");
        }
        try {
            ReviewMongoDB review = new ReviewMongoDB(txt, sentiment, movieId, userId);
            ReviewNeo4J review1 = new ReviewNeo4J(review.get_id(),sentiment);
            reviewMongoRepository.save(review);
            reviewNeo4JRepository.save(review1);
            reviewNeo4JRepository.setMovie(review.get_id(),movieId);
            userMongoDBRepository.setRecentReview(userId);
            return review;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReviewMongoDB> getAllReviews(int page) {
        if (page < 0) {throw new IllegalArgumentException("Page must be a positive integer");}
        try {
            return reviewMongoRepository.findAll(PageRequest.of(page, 100)).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReviewMongoDB> getReviewsByMovieId(String movieId) {
        if (!movieMongoDBRepository.existsById(movieId)) {
            throw new NoSuchElementException("Movie '" + movieId + "' doesn't exists");
        }
        try {
            return reviewMongoRepository.findByMovieId(movieId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReviewMongoDB> getReviewsByUsername(String userId) {
        if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        }
        try {
            return reviewMongoRepository.findByUsername(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ReviewMongoDB getReviewById(String id) {
        try {
            return reviewMongoRepository.findById(id).orElseThrow(NoSuchElementException::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteReview(String id,String userId) {
        if (!reviewMongoRepository.existsById(id)) {
            throw new NoSuchElementException("Review '" + userId + "' doesn't exists");
        } else if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        }
        try {
            reviewMongoRepository.deleteById(id);
            reviewNeo4JRepository.deleteById(id);
            userMongoDBRepository.setRecentReview(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // favoltativo
    public void updateReview(String id, String txt, boolean sentiment, String userId    ) {
        if (!reviewMongoRepository.existsById(id)) {
            throw new NoSuchElementException("Review '" + userId + "' doesn't exists");
        } else if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        }
        try {
            LocalDateTime time = LocalDateTime.now();
            reviewMongoRepository.updateReview(id,txt,sentiment,time);
            reviewNeo4JRepository.updateReview(id,sentiment);
            userMongoDBRepository.setRecentReview(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void likeReview(String id,String userId) {
        if (!reviewMongoRepository.existsById(id)) {
            throw new NoSuchElementException("Review '" + userId + "' doesn't exists");
        } else if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        }
        try {
            reviewMongoRepository.likeReview(id);
            reviewNeo4JRepository.likeReview(id,userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void unlikeReview(String id,String userId) {
        if (!reviewMongoRepository.existsById(id)) {
            throw new NoSuchElementException("Review '" + userId + "' doesn't exists");
        } else if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        }
        try {
            reviewMongoRepository.unlikeReview(id);
            reviewNeo4JRepository.unlikeReview(id,userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}