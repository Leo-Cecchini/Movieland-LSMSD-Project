package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.Movie.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieMongoDBRepository extends MongoRepository<Movie, String> {

    List<Movie> findAll();
    Optional<Movie> findById(String id);

    Movie insert(Movie movie);
    Movie save(Movie movie);
    void deleteById(String id);

    @Query(value = "{ 'type': {$eq: ?0 }, 'release_year': { $gte: 2015 }, 'imdb_votes': { $gte: 85 } }",
            sort = "{ 'imdb_score': -1, 'imdb_votes': -1 }",
            fields = "{ '_id': 1, 'title': 1, 'release_year': 1, 'imdb_score': 1, 'poster_path': 1 }")
    List<SearchTitleDTO> popularTitles(String type, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { genres: { $all: ?0 } } }",
            "{ $unwind: '$actors' }",
            "{ $group: { " + "_id: '$actors.id', " + "name: { $first: '$actors.name' }, " + "poster: { $first: '$actors.poster' }, " +
                    "movieCount: { $sum: 1 } " +
                    "} }",
            "{ $sort: { movieCount: -1 } }",
            "{ $limit: 5 }",
            "{ $project: { " + "actor_id: '$_id', " + "name: 1, " + "poster: 1, " + "movieCount: 1 " + "} }"
    })
    List<ActorDTO> mostFrequentActorsSpecificGenres(List<String> genres, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $sort: { imdb_votes: -1 } }",
            "{ $limit: 1000 }",
            "{ $unwind: \"$production_countries\" }",
            "{ $group: { _id: \"$production_countries\", count: { $sum: \"$imdb_votes\" } } }",
            "{ $sort: { count: -1 } }",
            "{ $project: { label: \"$_id\", count: 1 } }"
    })
    List<StringCountDTO> mostVotedMoviesByProductionCountries();

    @Aggregation(pipeline = {
            "{ $sort: { imdb_votes: -1 } }",
            "{ $limit: 1000 }",
            "{ $unwind: \"$keywords\" }",
            "{ $group: { _id: \"$keywords\", count: { $sum: \"$imdb_votes\" } } }",
            "{ $sort: { count: -1 } }",
            "{ $project: { label: \"$_id\", count: 1 } }"
    })
    List<StringCountDTO> mostVotedMoviesByKeywords();

    @Aggregation(pipeline = {
            "{ $sort: { imdb_votes: -1 } }",
            "{ $limit: 1000 }",
            "{ $unwind: \"$genres\" }",
            "{ $group: { _id: \"$genres\", count: { $sum: \"$imdb_votes\" } } }",
            "{ $sort: { count: -1 } }",
            "{ $project: { label: \"$_id\", count: 1 } }"
    })
    List<StringCountDTO> mostVotedMoviesByGenres(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $sort: { imdb_votes: -1 } }",
            "{ $limit: 100 }",
            "{ $lookup: { " + "from: 'Celebrities', " + "localField: '_id', " + "foreignField: 'jobs.movie_id', " +
                    "as: 'actors' " +
                    "} }",
            "{ $unwind: '$actors' }",
            "{ $group: { " + "_id: '$actors._id', " + "name: { $first: '$actors.name' }, " + "poster: { $first: '$actors.Poster' }, " +
                    "movieCount: { $sum: 1 } " +
                    "} }",
            "{ $sort: { movieCount: -1 } }",
            "{ $limit: 10 }",
            "{ $project: { " + "actor_id: '$_id', " + "name: 1, " + "poster: 1, " + "movieCount: 1 " + "} }"
    })
    List<ActorDTO> mostPopularActors(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $sort: { imdb_votes: -1 } }",
            "{ $limit: 1000 }",
            "{ $lookup: { " + "from: 'Celebrities', " + "localField: '_id', " + "foreignField: 'jobs.movie_id', " + "as: 'actors' " + "} }",
            "{ $unwind: '$actors' }",
            "{ $group: { " + "_id: '$actors._id', " + "name: { $first: '$actors.name' }, " + "poster: { $first: '$actors.Poster' }, " +
                    "avg_score: { $avg: '$imdb_score' }, " +
                    "movieCount: { $sum: 1 } " +
                    "} }",
            "{ $match: { movieCount: { $gte: 5 } } }",
            "{ $sort: { avg_score: -1 } }",
            "{ $limit: 10 }",
            "{ $project: { " + "actor_id: '$_id', " + "name: '$name', " + "poster: '$poster', " +
                    "averageScore: '$avg_score', " +
                    "movieCount: '$movieCount' " +
                    "} }"
    })
    List<ActorDTO> highesAverageActorsTop2000Movies();

    @Aggregation(pipeline = {
            "{ $unwind: '$platform' }",
            "{ $group: { " + "_id: '$platform', " + "count: { $sum: 1 } " + "} }",
            "{ $sort: { count: -1 } }",
            "{ $project: { " + "label: '$_id', " + "count: 1 " + "} }"
    })
    List<StringCountDTO> totalMoviesByPlatform();

    @Aggregation(pipeline = {
            "{ $addFields: { " + "profit: { $subtract: ['$revenue', '$budget'] } " + "} }",
            "{ $match: { profit: { $ne: null } } }",
            "{ $unwind: '$director' }",
            "{ $group: { " + "_id: '$director', " + "director: { $first: '$director.name' }, " + "average: { $avg: '$profit' } " + "} }",
            "{ $sort: { average: -1 } }",
            "{ $limit: 100 }",
            "{ $project: { " + "label: '$director', " + "count: '$average' " + "} }"
    })
    List<StringCountDTO> highestProfitDirectors();

    @Aggregation(pipeline = {
            "{ $sort: { imdb_votes: -1 } }",
            "{ $limit: 1000 }",
            "{ $unwind: '$platform' }",
            "{ $group: { " + "_id: '$platform', " + "count: { $sum: 1 } " + "} }",
            "{ $sort: { count: -1 } }",
            "{ $project: { " + "label: '$_id', " + "count: 1 " + "} }"
    })
    List<StringCountDTO> bestPlatformForTop1000Movies(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { genres: { $all: ?0 } } }",
            "{ $group: { _id: null, totalMovies: { $count: {} }, genreCounts: { $push: \"$genres\" } } }",
            "{ $unwind: \"$genreCounts\" }",
            "{ $unwind: \"$genreCounts\" }",
            "{ $group: { _id: \"$genreCounts\", movieCount: { $count: {} }, totalMovies: { $first: \"$totalMovies\" } } }",
            "{ $project: { label: \"$_id\", movieCount: 1, combinedPercentage: { $multiply: [ { $divide: [ \"$movieCount\", \"$totalMovies\" ] }, 100 ] } } }",
            "{ $sort: { combinedPercentage: -1 } }"
    })
    List<CombinedPercentageDTO> percentageOfCombinedGenres(List<String> genres);

    @Aggregation(pipeline = {
            "{ $match: { keywords: { $all: ?0 } } }",
            "{ $group: { _id: null, totalMovies: { $count: {} }, keywordCounts: { $push: \"$keywords\" } } }",
            "{ $unwind: \"$keywordCounts\" }",
            "{ $unwind: \"$keywordCounts\" }",
            "{ $group: { _id: \"$keywordCounts\", movieCount: { $count: {} }, totalMovies: { $first: \"$totalMovies\" } } }",
            "{ $project: { label: \"$_id\", movieCount: 1, combinedPercentage: { $multiply: [ { $divide: [ \"$movieCount\", \"$totalMovies\" ] }, 100 ] } } }",
            "{ $sort: { combinedPercentage: -1 } }"
    })
    List<CombinedPercentageDTO> percentageOfCombinedKeywords(List<String> keywords, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $project: { _id: 1 } }",    // Seleziona solo il campo _id
            "{ $group: { _id: 1, allIds: { $push: '$_id' } } }" // Raggruppa e crea l'array
    })
    public ListIdDTO findAllIds();

    @Aggregation(pipeline = {
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