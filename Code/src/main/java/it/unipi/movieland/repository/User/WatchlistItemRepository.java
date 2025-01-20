package applicationMovieland.repository.User;

import applicationMovieland.model.User.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, String> {
    // Puoi aggiungere metodi personalizzati se necessario
}