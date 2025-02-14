package it.unipi.movieland.service.Review;

import it.unipi.movieland.model.Review.ReviewMongoDB;
import it.unipi.movieland.model.Review.ReviewNeo4J;
import it.unipi.movieland.repository.Movie.Movie_mongoDB_interface;
import it.unipi.movieland.repository.Movie.Movie_mongoDB_repo;
import it.unipi.movieland.repository.Review.ReviewMongoDBRepository;
import it.unipi.movieland.repository.Review.ReviewNeo4JRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReviewService {

    @Autowired
    private ReviewMongoDBRepository reviewMongoRepository;

    @Autowired
    private ReviewNeo4JRepository reviewNeo4JRepository;

    @Autowired
    private UserMongoDBRepository userMongoDBRepository;

    @Autowired
    private Movie_mongoDB_interface movieMongoDBRepository;

    @Autowired
    private Movie_mongoDB_repo movieRepository;

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
            movieMongoDBRepository.updateReviews(movieId);
            return review;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReviewMongoDB> getAllReviews(int page, int size) {
        if (page < 0) {throw new IllegalArgumentException("Page must be a positive integer");}
        try {
            return reviewMongoRepository.findAll(PageRequest.of(page, size)).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReviewMongoDB> getReviewsByMovieId(String movieId, int page, int size) {
        if (!movieMongoDBRepository.existsById(movieId)) {
            throw new NoSuchElementException("Movie '" + movieId + "' doesn't exists");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return reviewMongoRepository.findByMovieId(movieId,(int)pageable.getOffset(),size);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReviewMongoDB> getReviewsByUsername(String userId, int page, int size) {
        if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return reviewMongoRepository.findByUsername(userId,(int)pageable.getOffset(),size);
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

    public void deleteReview(String id) {
        ReviewMongoDB review = getReviewById(id);
        if (review == null) {
            throw new NoSuchElementException("Review '" + id + "' doesn't exists");
        }
        try {
            reviewMongoRepository.deleteById(id);
            reviewNeo4JRepository.deleteById(id);
            userMongoDBRepository.setRecentReview(review.getUsername());
            movieMongoDBRepository.updateReviews(review.getMovie_id());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateReview(String id, String txt) {
        ReviewMongoDB review = getReviewById(id);
        if (review == null) {
            throw new NoSuchElementException("Review '" + id + "' doesn't exists");
        }
        try {
            LocalDateTime time = LocalDateTime.now();
            reviewMongoRepository.updateReview(id,txt,time);
            userMongoDBRepository.setRecentReview(review.getUsername());
            movieMongoDBRepository.updateReviews(review.getMovie_id());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void likeReview(String id,String userId) {
        if (!reviewMongoRepository.existsById(id)) {
            throw new NoSuchElementException("Review '" + userId + "' doesn't exists");
        } else if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        } else if (reviewNeo4JRepository.isReviewLiked(userId,id)) {
            throw new IllegalArgumentException("Review '" + id + "' is already liked");
        }
        try {
            reviewNeo4JRepository.likeReview(id,userId);
            reviewMongoRepository.likeReview(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void unlikeReview(String id,String userId) {
        if (!reviewMongoRepository.existsById(id)) {
            throw new NoSuchElementException("Review '" + userId + "' doesn't exists");
        } else if (!userMongoDBRepository.existsById(userId)) {
            throw new NoSuchElementException("User '" + userId + "' doesn't exists");
        } else if (!reviewNeo4JRepository.isReviewLiked(userId,id)) {
            throw new IllegalArgumentException("Review '" + id + "' isn't liked");
        }
        try {
            reviewNeo4JRepository.unlikeReview(id,userId);
            reviewMongoRepository.unlikeReview(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> findDifference(List<String> a, List<String> b) {
        Set<String> firstSet = new HashSet<>(a);

        List<String> result = new ArrayList<>();

        for (String mongoElement : b) {
            if (!firstSet.contains(mongoElement)) {
                result.add(mongoElement);
            }
        }

        return result;
    }

    public List<String> inconsistenciesNeo() {
        List<String> mongoDb=reviewMongoRepository.findAllCommentIds().getAllIds();
        List<String> neo4j=reviewNeo4JRepository.findAllIds();
        return findDifference(neo4j, mongoDb);
    }
    public List<String> inconsistenciesMongo() {
        List<String> mongoDb=reviewMongoRepository.findAllCommentIds().getAllIds();
        List<String> neo4j=reviewNeo4JRepository.findAllIds();
        return findDifference(mongoDb, neo4j);
    }

}