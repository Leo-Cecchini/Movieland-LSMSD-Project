package it.unipi.movieland.controller.Review;

import it.unipi.movieland.model.Review.Review;
import it.unipi.movieland.service.Review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Endpoint per ottenere tutte le recensioni
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // Endpoint per ottenere recensioni per un film specifico
    @GetMapping("/movie/{movieId}")
    public List<Review> getReviewsByMovieId(@PathVariable String movieId) {
        return reviewService.getReviewsByMovieId(movieId);
    }

    // Endpoint per ottenere una recensione specifica per ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere una nuova recensione
    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        Review addedReview = reviewService.addReview(review);
        return ResponseEntity.ok(addedReview);
    }

    // Endpoint per eliminare una recensione tramite ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}