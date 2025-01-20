package applicationMovieland.service.Movie;

import applicationMovieland.model.Movie.Movie;
import applicationMovieland.repository.Movie.MovieRepository;
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
        return movieRepository.getAllMovies();
    }

    // Ottieni un film tramite ID
    public Optional<Movie> getMovieById(String movieId) {
        return movieRepository.getMovieById(movieId);
    }

    // Aggiungi un nuovo film
    public Movie addMovie(Movie movie) {
        return movieRepository.addMovie(movie);
    }

    // Modifica un film esistente
    public Movie updateMovie(String movieId, Movie updatedMovie) {
        return movieRepository.updateMovie(movieId, updatedMovie);
    }

    // Elimina un film tramite ID
    public void deleteMovie(String movieId) {
        movieRepository.deleteMovie(movieId);
    }
}
