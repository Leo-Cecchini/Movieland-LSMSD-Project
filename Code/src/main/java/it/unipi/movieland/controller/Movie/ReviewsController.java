package it.unipi.movieland.controller.Movie;

import it.unipi.movieland.model.Movie.Reviews;
import it.unipi.movieland.service.Movie.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    @Autowired
    private ReviewsService reviewsService;

    // Endpoint per ottenere tutte le recensioni
    @GetMapping
    public List<Reviews> getAllReviews() {
        return reviewsService.getAllReviews();
    }

    // Endpoint per ottenere recensioni per un film specifico tramite ID
    @GetMapping("/{movieId}")
    public ResponseEntity<Reviews> getReviewsById(@PathVariable String movieId) {
        Optional<Reviews> reviews = reviewsService.getReviewsById(movieId);
        return reviews.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere una nuova recensione
    @PostMapping
    public ResponseEntity<Reviews> addReview(@RequestBody Reviews reviews) {
        Reviews addedReview = reviewsService.addReview(reviews);
        return ResponseEntity.ok(addedReview);
    }

    // Endpoint per aggiornare una recensione esistente
    @PutMapping("/{movieId}")
    public ResponseEntity<Reviews> updateReview(@PathVariable String movieId, @RequestBody Reviews updatedReviews) {
        Reviews updatedReview = reviewsService.updateReview(movieId, updatedReviews);
        return ResponseEntity.ok(updatedReview);
    }

    // Endpoint per eliminare una recensione
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String movieId) {
        reviewsService.deleteReview(movieId);
        return ResponseEntity.noContent().build();
    }
}
