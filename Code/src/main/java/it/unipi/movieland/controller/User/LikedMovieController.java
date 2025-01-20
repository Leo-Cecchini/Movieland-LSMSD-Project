package it.unipi.movieland.controller.User;

import it.unipi.movieland.model.User.LikedMovie;
import it.unipi.movieland.service.LikedMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/liked-movies")
public class LikedMovieController {

    @Autowired
    private LikedMovieService likedMovieService;

    // Endpoint per ottenere tutte le liked movies
    @GetMapping
    public List<LikedMovie> getAllLikedMovies() {
        return likedMovieService.getAllLikedMovies();
    }

    // Endpoint per ottenere un liked movie tramite filmId
    @GetMapping("/{filmId}")
    public ResponseEntity<LikedMovie> getLikedMovieById(@PathVariable String filmId) {
        Optional<LikedMovie> likedMovie = likedMovieService.getLikedMovieById(filmId);
        return likedMovie.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere un nuovo liked movie
    @PostMapping
    public ResponseEntity<LikedMovie> addLikedMovie(@RequestBody LikedMovie likedMovie) {
        LikedMovie addedMovie = likedMovieService.addLikedMovie(likedMovie);
        return ResponseEntity.ok(addedMovie);
    }

    // Endpoint per eliminare un liked movie tramite filmId
    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteLikedMovie(@PathVariable String filmId) {
        likedMovieService.deleteLikedMovie(filmId);
        return ResponseEntity.noContent().build();
    }

    // Puoi aggiungere altri metodi se necessario
}