package applicationMovieland.service.User;

import applicationMovieland.model.User.RecentReview;
import applicationMovieland.repository.User.RecentReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecentReviewService {

    @Autowired
    private RecentReviewRepository recentReviewRepository;

    // Aggiungi una nuova recensione
    public RecentReview addRecentReview(RecentReview recentReview) {
        return recentReviewRepository.save(recentReview);
    }

    // Ottieni tutte le recensioni
    public List<RecentReview> getAllRecentReviews() {
        return recentReviewRepository.findAll();
    }

    // Ottieni una recensione per reviewId
    public Optional<RecentReview> getRecentReviewById(int reviewId) {
        return recentReviewRepository.findById(reviewId);
    }

    // Rimuovi una recensione
    public void deleteRecentReview(int reviewId) {
        recentReviewRepository.deleteById(reviewId);
    }

    // Puoi aggiungere altre operazioni se necessario
}