package it.unipi.movieland.service.Review;

import it.unipi.movieland.dto.ReviewRatioDTO;
import it.unipi.movieland.exception.*;
import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.Review.ReviewMongoDB;
import it.unipi.movieland.model.Review.ReviewNeo4J;
import it.unipi.movieland.model.User.UserNeo4J;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.repository.Review.ReviewMongoDBRepository;
import it.unipi.movieland.repository.Review.ReviewNeo4JRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private MovieMongoDBRepository movieMongoDBRepository;

    //METHOD TO CREATE REVIEW
    @Transactional
    public ReviewMongoDB addReview(String movieId, String userId, String txt, boolean sentiment) {
        if (!userMongoDBRepository.existsById(userId)) {
            throw new UserNotFoundInMongoException("USER '" + userId + "' DOESN'T EXIST");
        }

        if (!movieMongoDBRepository.existsById(movieId)) {
            throw new MovieNotFoundInMongoException("MOVIE '" + movieId + "' DOESN'T EXIST");
        }

        try {
            ReviewMongoDB review = new ReviewMongoDB(txt, sentiment, movieId, userId);

            reviewMongoRepository.save(review);

            String reviewId = review.getId().toHexString();

            ReviewNeo4J reviewNeo = new ReviewNeo4J(reviewId, sentiment);
            reviewNeo4JRepository.save(reviewNeo);
            reviewNeo4JRepository.setMovie(reviewId, movieId);

            userMongoDBRepository.setRecentReview(userId);
            movieMongoDBRepository.updateReviews(movieId);

            return review;

        } catch (Exception e) {
            throw new RuntimeException("ERROR ADDING REVIEW: " + e.getMessage());
        }
    }

    //METHOD TO GET REVIEW BY ID
    public ReviewMongoDB getReviewById(String id) {
        try {
            return reviewMongoRepository.findById(id)
                    .orElseThrow(() -> new ReviewNotFoundInMongoException("REVIEW '" + id + "' NOT FOUND"));
        } catch (ReviewNotFoundInMongoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("ERROR RETRIEVING REVIEW BY ID: " + e.getMessage());
        }
    }

    //METHOD TO GET ALL REVIEWS
    public List<ReviewMongoDB> getAllReviews(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("PAGE MUST BE A POSITIVE INTEGER");
        }

        try {
            List<ReviewMongoDB> reviews = reviewMongoRepository.findAll(PageRequest.of(page, size)).getContent();

            if (reviews.isEmpty()) {
                throw new ReviewNotFoundInMongoException("NO REVIEWS FOUND.");
            }

            return reviews;

        } catch (ReviewNotFoundInMongoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("ERROR RETRIEVING REVIEWS: " + e.getMessage());
        }
    }

    //METHOD TO GET REVIEWS BY MOVIE ID
    public List<ReviewMongoDB> getReviewsByMovieId(String movieId, int page, int size) {

        if (!movieMongoDBRepository.existsById(movieId)) {
            throw new MovieNotFoundInMongoException("MOVIE WITH ID '" + movieId + "' DOES NOT EXIST.");
        }

        Pageable pageable = PageRequest.of(page, size);
        List<ReviewMongoDB> reviews = reviewMongoRepository.findByMovieId(movieId, (int) pageable.getOffset(), size);

        if (reviews == null || reviews.isEmpty()) {
            throw new ReviewNotFoundInMongoException("NO REVIEWS FOUND FOR MOVIE WITH ID '" + movieId + "'.");
        }

        return reviews;
    }

    //METHOD TO GET REVIEWS BY USERNAME
    public List<ReviewMongoDB> getReviewsByUsername(String userId, int page, int size) {
        if (!userMongoDBRepository.existsById(userId)) {
            throw new UserNotFoundInMongoException("USER '" + userId + "' DOESN'T EXIST");
        }

        try {
            Pageable pageable = PageRequest.of(page, size);
            return reviewMongoRepository.findByUsername(userId, (int) pageable.getOffset(), size);
        } catch (Exception e) {
            throw new RuntimeException("ERROR RETRIEVING REVIEWS BY USERNAME: " + e.getMessage());
        }
    }

    //METHOD TO UPDATE REVIEW BY ID
    @Transactional
    public void updateReview(String id, String txt) {
        ReviewMongoDB review = getReviewById(id);
        if (review == null) {
            throw new ReviewNotFoundInMongoException("REVIEW '" + id + "' DOESN'T EXIST");
        }

        try {
            LocalDateTime time = LocalDateTime.now();

            reviewMongoRepository.updateReview(id, txt, time);

            reviewNeo4JRepository.updateReview(id);

            userMongoDBRepository.setRecentReview(review.getUsername());
            movieMongoDBRepository.updateReviews(review.getMovieId());

        } catch (Exception e) {
            throw new RuntimeException("ERROR UPDATING REVIEW: " + e.getMessage());
        }
    }

    //METHOD TO DELETE A REVIEW
    @Transactional
    public void deleteReview(String id) {
        ReviewMongoDB review = getReviewById(id);
        if (review == null) {
            throw new ReviewNotFoundInMongoException("REVIEW '" + id + "' DOESN'T EXIST");
        }

        try {
            reviewMongoRepository.deleteById(id);

            reviewNeo4JRepository.deleteById(id);

            userMongoDBRepository.setRecentReview(review.getUsername());
            movieMongoDBRepository.updateReviews(review.getMovieId());

        } catch (Exception e) {
            throw new RuntimeException("ERROR DELETING REVIEW: " + e.getMessage());
        }
    }

    //METHOD TO LIKE A REVIEW
    @Transactional
    public void likeReview(String id, String userId) {
        if (!reviewMongoRepository.existsById(id)) {
            throw new ReviewNotFoundInMongoException("REVIEW '" + id + "' DOESN'T EXIST");
        }

        if (!userMongoDBRepository.existsById(userId)) {
            throw new UserNotFoundInMongoException("USER '" + userId + "' DOESN'T EXIST");
        }

        if (reviewNeo4JRepository.isReviewLiked(userId, id)) {
            throw new ReviewAlreadyLikedException("REVIEW '" + id + "' IS ALREADY LIKED BY USER '" + userId + ".");
        }

        try {
            reviewNeo4JRepository.likeReview(id, userId);
            reviewMongoRepository.likeReview(id);
        } catch (Exception e) {
            throw new RuntimeException("ERROR LIKING REVIEW: " + e.getMessage());
        }
    }

    //METHOD TO UNLIKE A REVIEW
    @Transactional
    public void unlikeReview(String id, String userId) {

        if (!reviewMongoRepository.existsById(id)) {
            throw new ReviewNotFoundInMongoException("REVIEW '" + id + "' DOESN'T EXIST");
        }

        if (!userMongoDBRepository.existsById(userId)) {
            throw new UserNotFoundInMongoException("USER '" + userId + "' DOESN'T EXIST");
        }

        if (!reviewNeo4JRepository.isReviewLiked(userId, id)) {
            throw new ReviewNotLikedException("REVIEW '" + id + "' IS NOT LIKED BY USER '" + userId + "'");
        }

        try {
            reviewNeo4JRepository.unlikeReview(id, userId);
            reviewMongoRepository.unlikeReview(id);
        } catch (Exception e) {
            throw new RuntimeException("ERROR UNLIKING REVIEW: " + e.getMessage(), e);
        }
    }

    // USER NEO4J MAPPER
    private UserNeo4J mapToUserNeo4J(Map<String, Object> props) {
        String username = (String) props.get("username");
        String name = (String) props.get("name");
        String surname = (String) props.get("surname");
        CountryEnum country = CountryEnum.valueOf(((String) props.get("country")).toUpperCase());

        List<String> genresRaw = (List<String>) props.get("favorite_genres");
        List<GenreEnum> genres = genresRaw.stream()
                .map(g -> {
                    try {
                        return GenreEnum.valueOf(g.toLowerCase());
                    } catch (IllegalArgumentException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return new UserNeo4J(username, name, surname, country, genres);
    }

    //METHOD TO FIND USERS WHO LIKED A REVIEW
    public List<UserNeo4J> findUserLikeReview(String reviewId) {
        if (!reviewNeo4JRepository.existsById(reviewId)) {
            throw new ReviewNotFoundInNeo4JException("REVIEW '" + reviewId + "' DOESN'T EXIST IN NEO4J");
        }

        try {
            List<Map<String, Object>> rawUsers = reviewNeo4JRepository.findUserLikeReview(reviewId);
            return rawUsers.stream().map(this::mapToUserNeo4J).toList();
        } catch (Exception e) {
            throw new RuntimeException("ERROR FINDING USERS WHO LIKED REVIEW '" + reviewId + "': " + e.getMessage(), e);
        }
    }

    //METHOD TO FIND TOP MOVIES BY REVIEW RATIO
    public List<ReviewRatioDTO> findTopMoviesByReviewRatio() {

        try {
            return reviewMongoRepository.findTopMoviesByReviewRatio();
        } catch (Exception e) {
            throw new RuntimeException("ERROR FINDING TOP MOVIES BY REVIEW RATIO: " + e.getMessage(), e);
        }
    }

    //METHOD TO FIND DIFFERENCE BETWEEN TWO LISTS
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

    //METHOD TO CHECK INCONSISTENCIES IN NEO4J
    public List<String> inconsistenciesNeo() {
        List<String> mongoDb = reviewMongoRepository.findAllCommentIds().getAllIds();
        List<String> neo4j = reviewNeo4JRepository.findAllIds();
        return findDifference(neo4j, mongoDb);
    }

    //METHOD TO CHECK INCONSISTENCIES IN MONGODB
    public List<String> inconsistenciesMongo() {
        List<String> mongoDb = reviewMongoRepository.findAllCommentIds().getAllIds();
        List<String> neo4j = reviewNeo4JRepository.findAllIds();
        return findDifference(mongoDb, neo4j);
    }
}