package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface Movie_mongoDB_interface extends MongoRepository<Movie, String> {

    List<Movie> findAll();
    Optional<Movie> findById(String id);

    //Optional <List<Review>> getReviewsByMovieId(String movieId);

    Movie insert(Movie movie);
    Movie save(Movie movie);
    void deleteById(String id);

    /*int addRole(String movie_id, Integer actor_id, String name, String character);
    int updateRole(String movie_id, Integer actor_id, String name, String character);
    int deleteRole(String movie_id, Integer actor_id);

    int addDirector(String movie_id, Integer director_id, String name);
    int updateDirector(String movie_id, Integer director_id, String name);
    int deleteDirector(String movie_id, Integer director_id);*/



}