package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    //List<Movie> findAll();

    //Optional<Movie> getMovieById(String movieId);

    //Movie addMovie(Movie movie);

    //Movie updateMovie(String movieId, Movie updatedMovie);

    //void deleteMovie(String movieId);
}