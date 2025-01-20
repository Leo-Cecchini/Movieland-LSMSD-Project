package it.unipi.movieland.service.Review;

import it.unipi.movieland.model.Rewiev.Review;
import it.unipi.movieland.repository.Review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Aggiungi una nuova recensione
    public Review addReview(Review review) {
        review.setTimestamp(java.time.LocalDateTime.now()); // Imposta la data di creazione
        return reviewRepository.save(review);
    }

    // Ottieni tutte le recensioni
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Ottieni recensioni per un film specifico
    public List<Review> getReviewsByMovieId(String movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    // Ottieni una recensione per ID
    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    // Elimina una recensione
    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }
}