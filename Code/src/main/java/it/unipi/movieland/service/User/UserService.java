package it.unipi.movieland.service.User;


import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.GenreEnum;
import it.unipi.movieland.model.User.UserCelebrity;
import it.unipi.movieland.model.User.UserMovie;
import it.unipi.movieland.model.User.UserMongoDB;
import it.unipi.movieland.model.User.UserNeo4J;
import it.unipi.movieland.repository.Celebrity.CelebrityNeo4JRepository;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.repository.Movie.MovieNeo4jRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import it.unipi.movieland.repository.User.UserNeo4JRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HexFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    public static String encrypt(String str, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(encryptedBytes);
    }

    @Autowired
    private UserMongoDBRepository mongoRepository;

    @Autowired
    private UserNeo4JRepository neoRepository;

    @Autowired
    private MovieMongoDBRepository movieMongoRepository;

    @Autowired
    private MovieNeo4jRepository movieNeoRepository;

    @Autowired
    private CelebrityNeo4JRepository celebrityNeoRepository;

    public List<UserMongoDB> getAllUsers(int page) {
        if (page < 0) {throw new IllegalArgumentException("Page must be a positive integer");}
        try {
            return mongoRepository.findAll(PageRequest.of(page, 100)).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserMongoDB addUser(String username, String email, String name, String surname, String password, String country, String phoneNumber, List<GenreEnum> favoriteGenres, String gender, LocalDate birthday) {
        if (mongoRepository.existsById(username) || neoRepository.existsById(username)) {
            throw new IllegalArgumentException("User '" + username + "' already exists");
        }
        try {
            String passwordE=encrypt(password,"MovieLand0123456");
            UserMongoDB mongoUser = new UserMongoDB(username, email, name, surname, passwordE, country, phoneNumber, favoriteGenres, gender, birthday);
            UserNeo4J neoUser = new UserNeo4J(username, name, surname, country, favoriteGenres);
            mongoRepository.save(mongoUser);
            neoRepository.save(neoUser);
            return mongoUser;
        } catch (Exception e) {
            mongoRepository.deleteById(username);
            throw new RuntimeException(e);
        }
    }

    // rendi facoltativi
    public void updateUser(String username, String email, String name, String surname, String country, String phoneNumber, List<GenreEnum> favoriteGenres, String gender) {
        if (!mongoRepository.existsById(username) && !neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        UserMongoDB userOld =mongoRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("User '" + username + "' doesn't exists"));
        try {
            mongoRepository.updateUser(username, email, name, surname, country, phoneNumber, favoriteGenres, gender);
            neoRepository.updateUser(username, name, surname, country, favoriteGenres);
        } catch (Exception e) {
            mongoRepository.updateUser(username, userOld.getEmail(), userOld.getName(), userOld.getSurname(), userOld.getCountry(), userOld.getPhone_number(), userOld.getFavorite_genres(), userOld.getGender());
            throw new RuntimeException(e);
        }
    }

    public UserMongoDB getUserByUsername(String username) {
        Optional<UserMongoDB> user = mongoRepository.findById(username);
        if (user.isEmpty()) {throw new NoSuchElementException("User " + username + " not found");}
        return user.get();
    }

    public void deleteUser(String username) {
        if (!mongoRepository.existsById(username) && !neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        UserMongoDB userOld =mongoRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("User '" + username + "' doesn't exists"));
        try {
            mongoRepository.deleteById(username);  // Rimuovi da MongoDB
            neoRepository.deleteById(username);    // Rimuovi da Neo4j
        } catch (Exception e) {
            mongoRepository.save(userOld);
            throw new RuntimeException(e);
        }
    }

    public boolean authenticate(String username, String password) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            String pass=mongoRepository.findById(username).get().getPassword();
            String passwordE=encrypt(password,"MovieLand0123456");
            return pass.equals(passwordE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WatchlistDTO getWatchlist(String username) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            UserMongoDB user= mongoRepository.getWatchlist(username);
            return new WatchlistDTO(user.getWatchlist());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addToWatchlist(String username, String movieId) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!movieMongoRepository.existsById(movieId)) {
            throw new NoSuchElementException("Movie '" + movieId + "' doesn't exists");
        } else if  (mongoRepository.isMovieInWatchlist(username, movieId)) {
            throw new IllegalArgumentException("Movie '" + movieId + "' is already in the watchlist");
        }
        try {
            mongoRepository.addToWatchlist(username,movieId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFromWatchlist(String username, String movieId) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!mongoRepository.isMovieInWatchlist(username, movieId)) {
            throw new IllegalArgumentException("Movie '" + movieId + "' is already in the watchlist");
        }
        try {
            mongoRepository.removeFromWatchlist(username,movieId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<UserMovie> getLikedMovies(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            return neoRepository.getLikedMovies(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addLikedMovie(String username, String movieId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!movieNeoRepository.existsById(movieId)) {
            throw new NoSuchElementException("Movie '" + movieId + "' doesn't exists");
        } else if (neoRepository.isMovieLiked(username, movieId)) {
            throw new IllegalArgumentException("Movie '" + movieId + "' is already liked");
        }
        try {
            neoRepository.addToLikedMovies(username,movieId);
            mongoRepository.addToLikedMovies(username, movieId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeLikedMovie(String username, String movieId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!movieNeoRepository.existsById(movieId)) {
            throw new NoSuchElementException("Movie '" + movieId + "' doesn't exists");
        } else if (!neoRepository.isMovieLiked(username, movieId)) {
            throw new IllegalArgumentException("Movie '" + movieId + "' isn't liked");
        }
        try {
            List<UserMovie> newMovies = neoRepository.removeFromLikedMovies(username,movieId);
            mongoRepository.removeFromLikedMovies(username,newMovies);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserCelebrity> getFollowedCelebrities(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            return neoRepository.getFollowedCelebrities(username);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void addFollowedCelebrity(String username, int celebrityId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!celebrityNeoRepository.existsById(String.valueOf(celebrityId))) {
            throw new NoSuchElementException("Celebrity '" + celebrityId + "' doesn't exists");
        } else if (neoRepository.isCelebrityFollowed(username, celebrityId)) {
            throw new IllegalArgumentException("Celebrity '" + celebrityId + "' is already followed");
        }
        try {
            neoRepository.addToFollowedCelebrities(username,celebrityId);
            mongoRepository.addToFollowedCelebrities(username, celebrityId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void removeFollowedCelebrity(String username, int celebrityId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!celebrityNeoRepository.existsById(String.valueOf(celebrityId))) {
            throw new NoSuchElementException("Celebrity '" + celebrityId + "' doesn't exists");
        } else if (!neoRepository.isCelebrityFollowed(username, celebrityId)) {
            throw new IllegalArgumentException("Celebrity '" + celebrityId + "' isn't followed");
        }
        try {
            List<UserCelebrity> newCelebrities = neoRepository.removeFromFollowedCelebrities(username,celebrityId);
            mongoRepository.removeFromFollowedCelebrities(username,newCelebrities);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<UserNeo4J> getFollowedUsers(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            return neoRepository.getFollowed(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserNeo4J> getFollowersUsers(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            return neoRepository.getFollowers(username);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void addFollowedUser(String username, String followedUsername) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!celebrityNeoRepository.existsById(String.valueOf(followedUsername))) {
            throw new NoSuchElementException("User '" + followedUsername + "' doesn't exists");
        } else if (neoRepository.isUserFollowed(username, followedUsername)) {
            throw new IllegalArgumentException("User '" + followedUsername + "' is already followed");
        }
        try {
            neoRepository.followUser(username,followedUsername);
            int a=neoRepository.countFollowed(username);
            int b=neoRepository.countFollowers(followedUsername);
            mongoRepository.updateFollowed(username, a);
            mongoRepository.updateFollowers(followedUsername, b);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void removeFollowedUser(String username, String followedUsername) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!celebrityNeoRepository.existsById(String.valueOf(followedUsername))) {
            throw new NoSuchElementException("User '" + followedUsername + "' doesn't exists");
        } else if (!neoRepository.isUserFollowed(username, followedUsername)) {
            throw new IllegalArgumentException("User '" + followedUsername + "' isn't followed");
        }
        try {
            neoRepository.unfollowUser(username,followedUsername);
            int a=neoRepository.countFollowed(username);
            int b=neoRepository.countFollowers(followedUsername);
            mongoRepository.updateFollowed(username, a);
            mongoRepository.updateFollowers(followedUsername, b);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<UserMongoDB> searchUser(String query,String country) {
        try {
            return mongoRepository.searchUser(query,country);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GenreRecommendationsDTO recommendByGenre(String username) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            return mongoRepository.recommendedMoviesGenre(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MovieRecommendationsDTO> recommendMovie(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            return neoRepository.recommendMovies(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<CelebrityRecommendationsDTO> recommendCelebrity(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            return neoRepository.recommendCelebrities(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserRecommendationsDTO> recommendUser(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            return neoRepository.recommendUsers(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MovieRecommendationsDTO> recommendCast(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            return neoRepository.recommendByCast(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MovieRecommendationsDTO> recommendReview(String username) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            return neoRepository.recommendByReview(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
