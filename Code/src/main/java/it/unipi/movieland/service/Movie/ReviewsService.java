package it.unipi.movieland.service.Movie;

import it.unipi.movieland.model.Movie.Reviews;
import it.unipi.movieland.repository.Movie.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewsService {

    @Autowired
    private ReviewsRepository reviewsRepository;

    // Ottieni tutte le recensioni
    public List<Reviews> getAllReviews() {
        return reviewsRepository.getAllReviews();
    }

    // Ottieni recensioni per un film specifico tramite ID
    public Optional<Reviews> getReviewsById(String movieId) {
        return reviewsRepository.getReviewsById(movieId);
    }

    // Aggiungi una nuova recensione
    public Reviews addReview(Reviews reviews) {
        return reviewsRepository.addReview(reviews);
    }

    // Modifica una recensione esistente
    public Reviews updateReview(String movieId, Reviews updatedReviews) {
        return reviewsRepository.updateReview(movieId, updatedReviews);
    }

    // Elimina una recensione
    public void deleteReview(String movieId) {
        reviewsRepository.deleteReview(movieId);
    }
}
