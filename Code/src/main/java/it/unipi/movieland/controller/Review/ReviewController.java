package it.unipi.movieland.controller.Review;

import it.unipi.movieland.exception.*;
import it.unipi.movieland.model.Review.ReviewMongoDB;
import it.unipi.movieland.model.User.UserNeo4J;
import it.unipi.movieland.service.Review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    //CREATE A NEW REVIEW FOR A MOVIE
    @PostMapping("/movie/{movieId}")
    public ResponseEntity<?> createReview(
            @PathVariable String movieId,
            @RequestParam String review,
            @RequestParam String userId,
            @RequestParam boolean sentiment) {
        try {
            ReviewMongoDB createdReview = reviewService.addReview(movieId, userId, review, sentiment);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "REVIEW CREATED SUCCESSFULLY.");
            response.put("review", createdReview);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UserNotFoundInMongoException | MovieNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "RESOURCE NOT FOUND", "details", e.getMessage()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO CREATE REVIEW", "details", e.getMessage()));
        }
    }

    //GET A REVIEW BY ID
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable String reviewId) {
        try {
            ReviewMongoDB review = reviewService.getReviewById(reviewId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "REVIEW RETRIEVED SUCCESSFULLY.");
            response.put("review", review);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ReviewNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "REVIEW NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET REVIEW", "details", e.getMessage()));
        }
    }

    //GET ALL REVIEWS WITH PAGINATION
    @GetMapping("/")
    public ResponseEntity<Object> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<ReviewMongoDB> reviews = reviewService.getAllReviews(page, size);

            if (reviews.isEmpty()) {
                throw new ReviewNotFoundInMongoException("NO REVIEWS FOUND.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "REVIEWS RETRIEVED SUCCESSFULLY.");
            response.put("reviews", reviews);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ReviewNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "REVIEW NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET REVIEWS", "details", e.getMessage()));
        }
    }

    //GET REVIEWS BY MOVIE ID WITH PAGINATION
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Object> getReviewsByMovie(
            @PathVariable String movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        try {
            List<ReviewMongoDB> reviews = reviewService.getReviewsByMovieId(movieId, page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "REVIEWS RETRIEVED SUCCESSFULLY.");
            response.put("reviews", reviews);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (MovieNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "MOVIE NOT FOUND", "details", e.getMessage()));

        } catch (ReviewNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "REVIEW NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET REVIEWS", "details", e.getMessage()));
        }
    }

    //GET ALL REVIEWS BY USER ID WITH PAGINATION
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<ReviewMongoDB> reviews = reviewService.getReviewsByUsername(userId, page, size);
            if (reviews.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(Map.of("message", "NO REVIEWS FOUND FOR USER '" + userId + "'"));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(reviews);
            }
        } catch (UserNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "USER NOT FOUND", "details", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO RETRIEVE REVIEWS", "details", e.getMessage()));
        }
    }

    //UPDATE AN EXISTING REVIEW BY REVIEW ID
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable String reviewId,
            @RequestParam String textReview) {
        try {
            reviewService.updateReview(reviewId, textReview);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "REVIEW UPDATED SUCCESSFULLY.");
            response.put("reviewId", reviewId);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ReviewNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "REVIEW NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO UPDATE REVIEW", "details", e.getMessage()));
        }
    }

    //DELETE A REVIEW BY ID
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable String reviewId) {
        try {
            reviewService.deleteReview(reviewId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "REVIEW DELETED SUCCESSFULLY.");
            response.put("reviewId", reviewId);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ReviewNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "REVIEW NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO DELETE REVIEW", "details", e.getMessage()));
        }
    }

    //LIKE A REVIEW BY ID
    @PutMapping("/{reviewId}/like")
    public ResponseEntity<?> likeReview(
            @PathVariable String reviewId,
            @RequestParam String userId) {
        try {
            reviewService.likeReview(reviewId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "REVIEW LIKED SUCCESSFULLY.");
            response.put("reviewId", reviewId);
            response.put("userId", userId);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ReviewNotFoundInMongoException | UserNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "RESOURCE NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO LIKE REVIEW", "details", e.getMessage()));
        }
    }

    //UNLIKE A REVIEW BY ID
    @PutMapping("/{reviewId}/unlike")
    public ResponseEntity<?> unlikeReview(
            @PathVariable String reviewId,
            @RequestParam String userId) {
        try {
            reviewService.unlikeReview(reviewId, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "REVIEW UNLIKED SUCCESSFULLY.");
            response.put("reviewId", reviewId);
            response.put("userId", userId);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ReviewNotFoundInMongoException | UserNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "RESOURCE NOT FOUND", "details", e.getMessage()));

        } catch (ReviewNotLikedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "REVIEW NOT LIKED", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO UNLIKE REVIEW", "details", e.getMessage()));
        }
    }

    //GET ALL USERS WHO LIKED A SPECIFIC REVIEW BY ID
    @GetMapping("/{reviewId}/likes")
    public ResponseEntity<?> getUsersWhoLikedReview(
            @PathVariable String reviewId) {
        try {
            List<UserNeo4J> users = reviewService.findUserLikeReview(reviewId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "LIKES RETRIEVED SUCCESSFULLY.");
            response.put("reviewId", reviewId);
            response.put("likesCount", users.size());
            response.put("users", users);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ReviewNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "REVIEW NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET LIKES", "details", e.getMessage()));
        }
    }
}