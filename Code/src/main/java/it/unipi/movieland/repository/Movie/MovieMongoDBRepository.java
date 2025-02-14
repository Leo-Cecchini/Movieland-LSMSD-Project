package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.dto.ActorDTO;
import it.unipi.movieland.dto.CombinedPercentageDTO;
import it.unipi.movieland.dto.StringCountDTO;
import it.unipi.movieland.model.Movie.Movie;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
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
    List<ActorDTO> mostFrequentActorsSpecificGenres(List<String> genres);

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
    List<StringCountDTO> mostVotedMoviesByGenres();

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
    List<ActorDTO> mostPopularActors();

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
    List<StringCountDTO> bestPlatformForTop1000Movies();

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
    List<CombinedPercentageDTO> percentageOfCombinedKeywords(List<String> keywords);

}