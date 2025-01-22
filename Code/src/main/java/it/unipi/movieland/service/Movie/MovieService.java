package it.unipi.movieland.service.Movie;

import it.unipi.movieland.model.Movie.Movie;
import it.unipi.movieland.repository.Movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Ottieni tutti i film
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Ottieni un film tramite ID
    public Optional<Movie> getMovieById(String movieId) {
        return movieRepository.findById(movieId);
    }

    // Aggiungi un nuovo film
    public Movie addMovie(Movie movie) {
        return movieRepository.insert(movie);
    }

    // Modifica un film esistente
    public Movie updateMovie(Movie updatedMovie) {
        return movieRepository.save(updatedMovie);
    }

    // Elimina un film tramite ID
    public void deleteMovie(String movieId) {
        movieRepository.deleteById(movieId);
    }
}
