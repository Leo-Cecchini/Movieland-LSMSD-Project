package it.unipi.movieland.controller.User;

import it.unipi.movieland.model.User.RecentReview;
import it.unipi.movieland.service.User.RecentReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recent-reviews")
public class RecentReviewController {

    @Autowired
    private RecentReviewService recentReviewService;

    // Endpoint per ottenere tutte le recensioni
    @GetMapping
    public List<RecentReview> getAllRecentReviews() {
        return recentReviewService.getAllRecentReviews();
    }

    // Endpoint per ottenere una recensione per reviewId
    @GetMapping("/{reviewId}")
    public ResponseEntity<RecentReview> getRecentReviewById(@PathVariable int reviewId) {
        Optional<RecentReview> recentReview = recentReviewService.getRecentReviewById(reviewId);
        return recentReview.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere una nuova recensione
    @PostMapping
    public ResponseEntity<RecentReview> addRecentReview(@RequestBody RecentReview recentReview) {
        RecentReview addedReview = recentReviewService.addRecentReview(recentReview);
        return ResponseEntity.ok(addedReview);
    }

    // Endpoint per eliminare una recensione
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteRecentReview(@PathVariable int reviewId) {
        recentReviewService.deleteRecentReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // Puoi aggiungere altri metodi se necessario
}
