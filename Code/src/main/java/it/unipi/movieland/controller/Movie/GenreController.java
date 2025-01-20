package it.unipi.movieland.controller.Movie;

import it.unipi.movieland.model.Movie.Genre;
import it.unipi.movieland.service.Movie.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    // Endpoint per ottenere tutti i generi
    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    // Endpoint per aggiungere un nuovo genere
    @PostMapping
    public ResponseEntity<Genre> addGenre(@RequestBody Genre genre) {
        Genre addedGenre = genreService.addGenre(genre);
        return ResponseEntity.ok(addedGenre);
    }

    // Puoi aggiungere altri endpoint se necessario, per esempio per eliminare un genere
}