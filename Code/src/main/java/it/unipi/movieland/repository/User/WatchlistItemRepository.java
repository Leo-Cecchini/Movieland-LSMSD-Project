package it.unipi.movieland.repository.User;

import it.unipi.movieland.model.User.WatchlistItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistItemRepository extends MongoRepository<WatchlistItem, String> {
    // Puoi aggiungere metodi personalizzati se necessario
}