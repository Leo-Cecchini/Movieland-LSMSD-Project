package it.unipi.movieland.controller.User;

import it.unipi.movieland.model.User.WatchlistItem;
import it.unipi.movieland.service.WatchlistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/watchlist")
public class WatchlistItemController {

    @Autowired
    private WatchlistItemService watchlistItemService;

    // Endpoint per ottenere tutti gli elementi nella watchlist
    @GetMapping
    public List<WatchlistItem> getAllWatchlistItems() {
        return watchlistItemService.getAllWatchlistItems();
    }

    // Endpoint per ottenere un elemento della watchlist per filmId
    @GetMapping("/{filmId}")
    public ResponseEntity<WatchlistItem> getWatchlistItemByFilmId(@PathVariable String filmId) {
        Optional<WatchlistItem> watchlistItem = watchlistItemService.getWatchlistItemByFilmId(filmId);
        return watchlistItem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere un nuovo elemento alla watchlist
    @PostMapping
    public ResponseEntity<WatchlistItem> addWatchlistItem(@RequestBody WatchlistItem watchlistItem) {
        WatchlistItem addedItem = watchlistItemService.addWatchlistItem(watchlistItem);
        return ResponseEntity.ok(addedItem);
    }

    // Endpoint per rimuovere un elemento dalla watchlist
    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteWatchlistItem(@PathVariable String filmId) {
        watchlistItemService.deleteWatchlistItem(filmId);
        return ResponseEntity.noContent().build();
    }

    // Puoi aggiungere altri metodi se necessario
}
