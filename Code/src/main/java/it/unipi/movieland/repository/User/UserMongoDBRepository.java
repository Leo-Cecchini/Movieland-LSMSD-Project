package it.unipi.movieland.repository.User;

import it.unipi.movieland.dto.GenreRecommendationsDTO;
import it.unipi.movieland.dto.ListIdDTO;
import it.unipi.movieland.model.CountryEnum;
import it.unipi.movieland.model.GenderEnum;
import it.unipi.movieland.model.GenreEnum;
import it.unipi.movieland.model.User.UserCelebrity;
import it.unipi.movieland.model.User.UserMongoDB;
import it.unipi.movieland.model.User.UserMovie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface UserMongoDBRepository extends MongoRepository<UserMongoDB, String> {

    @Query("{ '_id': ?0 }")
    @Update("{ $set: { 'name': ?1, 'surname': ?2, 'country': ?3, 'phone_number': ?4, 'favorite_genres': ?5, 'gender': ?6 } }")
    void updateUser(String username, String name, String surname, CountryEnum country, String phoneNumber, List<GenreEnum> favoriteGenres, GenderEnum gender);

    @Query(value = "{ '_id': ?0 }", fields = "{ 'watchlist': 1 }")
    UserMongoDB getWatchlist(String userId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }", // Trova l'utente tramite userId
            "{ $lookup: { from: 'Movies', let: { movieId: ?1 }, pipeline: [ { $match: { $expr: { $eq: ['$_id', '$$movieId'] } } } ], as: 'watchMovie' } }",
            "{ $set: { watchMovie: { $first: '$watchMovie' } } }", // Prende il primo (e unico) film trovato
            "{ $set: { watchlist: { $concatArrays: ['$watchlist', [{ film_id: '$watchMovie._id', title: '$watchMovie.title', poster: '$watchMovie.poster_path' }]] } } }",
            "{ $merge: { into: 'Users', whenMatched: 'merge', whenNotMatched: 'insert' } }"
    })
    void addToWatchlist(String userId, String movieId);

    @Query("{ '_id': ?0 }")
    @Update("{ $pull: { 'watchlist': { 'film_id': ?1 } } }")
    void removeFromWatchlist(String userId, String movieId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }", // Trova l'utente tramite userId
            "{ $lookup: { from: 'Movies', let: { movieId: ?1 }, pipeline: [ { $match: { $expr: { $eq: ['$_id', '$$movieId'] } } } ], as: 'likedMovie' } }",
            "{ $set: { likedMovie: { $first: '$likedMovie' } } }", // Prende il primo (e unico) film trovato
            "{ $set: { liked_movies: { $concatArrays: ['$liked_movies', [{ film_id: '$likedMovie._id', title: '$likedMovie.title', poster: '$likedMovie.poster_path' }]] } } }",
            "{ $set: { liked_movies: { $slice: ['$liked_movies', -5] } } }", // Mantieni solo gli ultimi 5 film
            "{ $merge: { into: 'Users', whenMatched: 'merge', whenNotMatched: 'insert' } }"
    })
    void addToLikedMovies(String userId, String movieId);

    @Aggregation(pipeline = {
            "{ '$match': { '_id': ?0 } }",
            "{ '$lookup': { 'from': 'Reviews', 'let': { 'username': '$_id' }, 'pipeline': [{ '$match': { '$expr': { '$eq': [ '$username', '$$username' ] } } } , { '$sort': { 'timestamp': -1 } }, { '$limit': 1 }, { '$lookup': { 'from': 'Movies', 'localField': 'movie_id', 'foreignField': '_id', 'as': 'movie' } }, { '$unwind': '$movie' }, { '$project': { 'review_id': '$_id', 'movie_title': '$movie.title', 'sentiment': '$sentiment', 'content': '$review' } } ], 'as': 'recent_review' } }",
            "{ '$set': { 'recent_review': { '$arrayElemAt': [ '$recent_review', 0 ] } } }",
            "{'$unset': 'recent_review._id'}",
            "{ '$merge': { 'into': 'Users', 'on': '_id', 'whenMatched': 'replace', 'whenNotMatched': 'discard' } }"
    })
    void setRecentReview(String userId);

    @Query("{ '_id': ?0 }")
    @Update("{ $set: { 'liked_movies': ?1 } }")
    void removeFromLikedMovies(String userId, List<UserMovie> movies);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $lookup: { from: 'Celebrities', let: { celebrityId: ?1 }, pipeline: [ { $match: { $expr: { $eq: ['$_id', '$$celebrityId'] } } } ], as: 'followedCelebrity' } }",
            "{ $set: { followedCelebrity: { $first: '$followedCelebrity' } } }",
            "{ $set: { followed_celebrities: { $concatArrays: ['$followed_celebrities', [{ person_id: '$followedCelebrity._id', name: '$followedCelebrity.name', poster: '$followedCelebrity.Poster'}]] } } }",
            "{ $set: { followed_celebrities: { $slice: ['$followed_celebrities', -5] } } }",
            "{ $merge: { into: 'Users', whenMatched: 'merge', whenNotMatched: 'insert' } }"
    })
    void addToFollowedCelebrities(String userId, int celebrityId);

    @Query("{ '_id': ?0 }")
    @Update("{ $set: { 'followed_celebrities': ?1 } }")
    void removeFromFollowedCelebrities(String userId, List<UserCelebrity> celebrities);

    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'followed': 1 } }")
    void increaseFollowed(String userId);

    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'follower': 1 } }")
    void increaseFollowers(String userId);

    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'followed': -1 } }")
    void decreaseFollowed(String userId);

    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'follower': -1 } }")
    void decreaseFollowers(String userId);

    @Aggregation(pipeline = {
            "{ $match: { $text: { $search: '?0' }, $or: [ { 'country': '?1' }, { '?1': null }, { '?1': '' } ] } }",
            "{ $sort: { score: { $meta: 'textScore' } } }",
            "{ $skip: ?2 }",
            "{ $limit: ?3 }"
    })
    List<UserMongoDB> searchUser(String query, String country, int offset, int limit);

    @Aggregation(pipeline = {
            "{ $match: { _id: '?0' } }",
            "{ $lookup: { from: 'Movies', let: { userGenres: '$favorite_genres', likedMovies: { $map: { input: '$liked_movies', as: 'm', in: '$$m.film_id' } } }, pipeline: [ " +
                    "{ $match: { $expr: { $and: [ { $or: [ { $not: { $in: ['$_id', '$$likedMovies'] } }, { $eq: [{ $size: '$$likedMovies' }, 0] } ] }, { $gt: [{ $size: { $setIntersection: ['$genres', '$$userGenres'] } }, 0 ] } ] } } } , " +
                    "{ $addFields: { match_score: { $size: { $setIntersection: ['$genres', '$$userGenres'] } } } }, " +
                    "{ $project: { _id: 1, title: 1, poster_path: 1,imdb_votes:1, match_score: 1 } } ], as: 'matched_movies' } }",
            "{ $unwind: '$matched_movies' }",
            "{ $sort: { 'matched_movies.match_score': -1, 'matched_movies.imdb_votes': -1 } }",
            "{ $group: { _id: '$_id',  recommended_movies: { $push: '$matched_movies' } } }",
            "{ $project: { _id: 1, recommended_movies: { $slice: ['$recommended_movies', 10] } } }"
    })
    GenreRecommendationsDTO recommendedMoviesGenre(String userId);

    @Query(value = "{ '_id': ?0, 'watchlist.film_id': ?1 }", exists = true)
    boolean isMovieInWatchlist(String username, String movieId);

    @Aggregation(pipeline = {
            "{ $project: { _id: 1 } }",    // Seleziona solo il campo _id
            "{ $group: { _id: 1, allIds: { $push: '$_id' } } }" // Raggruppa e crea l'array
    })
    ListIdDTO findAllMovieIds();

    boolean existsByEmail(String email);
}
