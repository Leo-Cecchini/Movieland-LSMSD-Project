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
    public ResponseEntity<?> getAllReviews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            List<ReviewMongoDB> reviews = reviewService.getAllReviews(page,size);
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
    public ResponseEntity<?> getReviews(@PathVariable String movieId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            List<ReviewMongoDB> reviews = reviewService.getReviewsByMovieId(movieId,page,size);
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

    @GetMapping("/id/{reviewId}")
    public ResponseEntity<?> getReview(@PathVariable String reviewId) {
        try {
            return new ResponseEntity<>(reviewService.getReviewById(reviewId),HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/id/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable String reviewId, String review) {
        try {
            reviewService.updateReview(reviewId,review);
            return new ResponseEntity<>("Review updated",HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/id/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable String reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return new ResponseEntity<>("Review deleted",HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/id/{reviewId}/like")
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

    @PutMapping("/id/{reviewId}/unlike")
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
    public ResponseEntity<?> getUserReviews(@PathVariable String userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            List<ReviewMongoDB> reviews = reviewService.getReviewsByUsername(userId,page,size);
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