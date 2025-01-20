package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.Movie;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepository {

    private List<Movie> movies = new ArrayList<>();

    // Ottieni tutti i film
    public List<Movie> getAllMovies() {
        return movies;
    }

    // Ottieni un film tramite ID
    public Optional<Movie> getMovieById(String movieId) {
        return movies.stream().filter(movie -> movie.get_id().equals(movieId)).findFirst();
    }

    // Aggiungi un nuovo film
    public Movie addMovie(Movie movie) {
        movies.add(movie);
        return movie;
    }

    // Modifica un film esistente
    public Movie updateMovie(String movieId, Movie updatedMovie) {
        Movie existingMovie = getMovieById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        existingMovie.setGenres(updatedMovie.getGenres());
        existingMovie.setKeywords(updatedMovie.getKeywords());
        existingMovie.setProductionCountries(updatedMovie.getProductionCountries());
        existingMovie.setRuntime(updatedMovie.getRuntime());
        existingMovie.setPosterPath(updatedMovie.getPosterPath());
        existingMovie.setImdbScore(updatedMovie.getImdbScore());
        existingMovie.setImdbVotes(updatedMovie.getImdbVotes());
        existingMovie.setPlatform(updatedMovie.getPlatform());
        existingMovie.setActors(updatedMovie.getActors());
        existingMovie.setReviews(updatedMovie.getReviews());
        existingMovie.setRevenue(updatedMovie.getRevenue());
        existingMovie.setBudget(updatedMovie.getBudget());
        existingMovie.setAgeCertification(updatedMovie.getAgeCertification());
        existingMovie.setSeasons(updatedMovie.getSeasons());
        return existingMovie;
    }

    // Elimina un film tramite ID
    public void deleteMovie(String movieId) {
        Movie movie = getMovieById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        movies.remove(movie);
    }
}