package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.dto.ListIdDTO;
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
    @org.springframework.data.mongodb.repository.Aggregation(pipeline = {
            "{ $project: { _id: 1 } }",    // Seleziona solo il campo _id
            "{ $group: { _id: 1, allIds: { $push: '$_id' } } }" // Raggruppa e crea l'array
    })
    public ListIdDTO findAllIds();

    @org.springframework.data.mongodb.repository.Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $lookup: { from: 'Reviews', localField: '_id', foreignField: 'movie_id', as: 'reviews' } }",
            "{ $unwind: '$reviews' }",
            "{ $sort: { 'reviews.timestamp': -1 } }",
            "{ $limit: 5 }",
            "{ $group: { _id: '$_id', title: { $first: '$title' }, type: { $first: '$type' }, description: { $first: '$description' }, release_year: { $first: '$release_year' }, genres: { $first: '$genres' }, keywords: { $first: '$keywords' }, production_countries: { $first: '$production_countries' }, runtime: { $first: '$runtime' }, poster_path: { $first: '$poster_path' }, imdb_score: { $first: '$imdb_score' }, imdb_votes: { $first: '$imdb_votes' }, platform: { $first: '$platform' }, director: { $first: '$director' }, actors: { $first: '$actors' }, reviews: { $push: { id: '$reviews._id', review: '$reviews.review', sentiment: '$reviews.sentiment', username: '$reviews.username', timestamp: '$reviews.timestamp' } }, revenue: { $first: '$revenue' }, budget: { $first: '$budget' }, age_certification: { $first: '$age_certification' }, seasons: { $first: '$seasons' } } }",
            "{ $merge: { into: 'Movies', whenMatched: 'merge', whenNotMatched: 'insert' } }"
    })
    public void updateReviews(String movieId);



}