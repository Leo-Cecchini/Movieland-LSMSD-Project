package it.unipi.movieland.controller.Review;

import it.unipi.movieland.model.Review.ReviewMongoDB;
import it.unipi.movieland.model.User.UserMongoDB;
import it.unipi.movieland.service.Review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public ResponseEntity<?> getAllReviews(@RequestParam int page) {
        try {
            List<ReviewMongoDB> reviews = reviewService.getAllReviews(page);
            if (reviews.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(reviews, HttpStatus.OK);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getReviews(@PathVariable String movieId) {
        try {
            List<ReviewMongoDB> reviews = reviewService.getReviewsByMovieId(movieId);
            if (reviews.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(reviews, HttpStatus.OK);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/movie/{movieId}")
    public ResponseEntity<?> addReview(@PathVariable String movieId, String review, String userId, boolean sentiment) {
        try {
            return new ResponseEntity<>(reviewService.addReview(movieId,userId,review,sentiment),HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReview(@PathVariable String reviewId) {
        try {
            return new ResponseEntity<>(reviewService.getReviewById(reviewId),HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable String reviewId, String review, boolean sentiment, String username) {
        try {
            reviewService.updateReview(reviewId,review,sentiment,username);
            return new ResponseEntity<>("Review updated",HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable String reviewId,String userId) {
        try {
            reviewService.deleteReview(reviewId,userId);
            return new ResponseEntity<>("Review deleted",HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reviewId}/like")
    public ResponseEntity<String> likeReview(@PathVariable String reviewId, String userId) {
        try {
            reviewService.likeReview(reviewId,userId);
            return new ResponseEntity<>("Review liked",HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reviewId}/unlike")
    public ResponseEntity<String> unlikeReview(@PathVariable String reviewId, String userId) {
        try {
            reviewService.unlikeReview(reviewId,userId);
            return new ResponseEntity<>("Review unliked",HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReviews(@PathVariable String userId) {
        try {
            List<ReviewMongoDB> reviews = reviewService.getReviewsByUsername(userId);
            if (reviews.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(reviews, HttpStatus.OK);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}