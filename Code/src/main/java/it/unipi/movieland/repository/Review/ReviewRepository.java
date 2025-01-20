package applicationMovieland.repository.Review;

import applicationMovieland.model.Rewiev.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    // Puoi aggiungere metodi personalizzati, ad esempio per ottenere tutte le recensioni per un film
    List<Review> findByMovieId(String movieId);

    // Aggiungi altri metodi come necessario
}