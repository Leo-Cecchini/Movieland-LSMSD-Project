package it.unipi.movieland.controller.Movie;

import it.unipi.movieland.model.Movie.Movie;
import it.unipi.movieland.service.Movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Endpoint per ottenere tutti i film
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    // Endpoint per ottenere un film tramite ID
    @GetMapping("/{movieId}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String movieId) {
        Optional<Movie> movie = movieService.getMovieById(movieId);
        System.out.println(movie.toString());
        return movie.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere un nuovo film
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie addedMovie = movieService.addMovie(movie);
        return ResponseEntity.ok(addedMovie);
    }

    // Endpoint per aggiornare un film esistente
    @PutMapping("/{movieId}")
    public ResponseEntity<Movie> updateMovie(@RequestBody Movie updatedMovie) {
        Movie movie = movieService.updateMovie(updatedMovie);
        return ResponseEntity.ok(movie);
    }

    // Endpoint per eliminare un film tramite ID
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }
}