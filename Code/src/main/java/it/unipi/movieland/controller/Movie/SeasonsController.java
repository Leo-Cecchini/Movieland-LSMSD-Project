package applicationMovieland.controller.Movie;

import applicationMovieland.model.Movie.Seasons;
import applicationMovieland.service.Movie.SeasonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/seasons")
public class SeasonsController {

    @Autowired
    private SeasonsService seasonsService;

    // Endpoint per ottenere tutte le stagioni
    @GetMapping
    public List<Seasons> getAllSeasons() {
        return seasonsService.getAllSeasons();
    }

    // Endpoint per ottenere le stagioni per un film/serie tramite ID
    @GetMapping("/{movieId}")
    public ResponseEntity<Seasons> getSeasonsById(@PathVariable String movieId) {
        Optional<Seasons> seasons = seasonsService.getSeasonsById(movieId);
        return seasons.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere nuove stagioni
    @PostMapping
    public ResponseEntity<Seasons> addSeasons(@RequestBody Seasons seasons) {
        Seasons addedSeasons = seasonsService.addSeasons(seasons);
        return ResponseEntity.ok(addedSeasons);
    }

    // Endpoint per aggiornare una stagione esistente
    @PutMapping("/{movieId}")
    public ResponseEntity<Seasons> updateSeasons(@PathVariable String movieId, @RequestBody Seasons updatedSeasons) {
        Seasons updatedSeason = seasonsService.updateSeasons(movieId, updatedSeasons);
        return ResponseEntity.ok(updatedSeason);
    }

    // Endpoint per eliminare una stagione
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteSeasons(@PathVariable String movieId) {
        seasonsService.deleteSeasons(movieId);
        return ResponseEntity.noContent().build();
    }
}