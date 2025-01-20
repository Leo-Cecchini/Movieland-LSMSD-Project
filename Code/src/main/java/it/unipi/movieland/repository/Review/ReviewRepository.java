package it.unipi.movieland.repository.Review;

import it.unipi.movieland.model.Rewiev.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    // Puoi aggiungere metodi personalizzati, ad esempio per ottenere tutte le recensioni per un film
    List<Review> findByMovieId(String movieId);

    // Aggiungi altri metodi come necessario
}