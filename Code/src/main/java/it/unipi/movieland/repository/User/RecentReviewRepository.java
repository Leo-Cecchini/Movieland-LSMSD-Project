package it.unipi.movieland.repository.User;

import it.unipi.movieland.model.User.RecentReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentReviewRepository extends MongoRepository<RecentReview, Integer> {
    // Puoi aggiungere metodi personalizzati se necessario
}