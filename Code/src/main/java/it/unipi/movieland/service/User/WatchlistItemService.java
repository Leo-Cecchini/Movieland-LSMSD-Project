package applicationMovieland.service.User;

import applicationMovieland.model.User.WatchlistItem;
import applicationMovieland.repository.User.WatchlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistItemService {

    @Autowired
    private WatchlistItemRepository watchlistItemRepository;

    // Aggiungi un nuovo elemento alla watchlist
    public WatchlistItem addWatchlistItem(WatchlistItem watchlistItem) {
        return watchlistItemRepository.save(watchlistItem);
    }

    // Ottieni tutti gli elementi della watchlist
    public List<WatchlistItem> getAllWatchlistItems() {
        return watchlistItemRepository.findAll();
    }

    // Ottieni un elemento della watchlist per filmId
    public Optional<WatchlistItem> getWatchlistItemByFilmId(String filmId) {
        return watchlistItemRepository.findById(filmId);
    }

    // Rimuovi un elemento dalla watchlist
    public void deleteWatchlistItem(String filmId) {
        watchlistItemRepository.deleteById(filmId);
    }

    // Puoi aggiungere altre operazioni di business se necessario
}
