package it.unipi.movieland.service.User;

import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.CountryEnum;
import it.unipi.movieland.model.GenderEnum;
import it.unipi.movieland.model.GenreEnum;
import it.unipi.movieland.model.User.UserCelebrity;
import it.unipi.movieland.model.User.UserMovie;
import it.unipi.movieland.model.User.UserMongoDB;
import it.unipi.movieland.model.User.UserNeo4J;
import it.unipi.movieland.repository.Celebrity.CelebrityMongoDBRepository;
import it.unipi.movieland.repository.Celebrity.CelebrityNeo4JRepository;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.repository.Movie.MovieNeo4jRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import it.unipi.movieland.repository.User.UserNeo4JRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

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
    private CelebrityMongoDBRepository celebrityMongoRepository;

    @Autowired
    private CelebrityNeo4JRepository celebrityNeoRepository;

    public List<UserMongoDB> getAllUsers(int page,int size) {
        if (page < 0) {throw new IllegalArgumentException("Page must be a positive integer");}
        try {
            return mongoRepository.findAll(PageRequest.of(page, size)).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public UserMongoDB addUser(String username, String email, String name, String surname, String password, CountryEnum country, String phoneNumber, List<GenreEnum> favoriteGenres, GenderEnum gender, LocalDate birthday) {
        if (mongoRepository.existsById(username) || neoRepository.existsById(username)) {
            throw new IllegalArgumentException("User '" + username + "' already exists");
        } else if (mongoRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Mail'" + email + "' already used");
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

    @Transactional
    public void updateUser(String username, String name, String surname, CountryEnum country, String phoneNumber, List<GenreEnum> favoriteGenres, GenderEnum gender) {
        Optional<UserMongoDB> userOld = mongoRepository.findById(username);
        if (userOld.isEmpty()) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        if (name == null) {name = userOld.get().getName();}
        if (surname == null) {surname = userOld.get().getSurname();}
        if (country == null) {country = userOld.get().getCountry();}
        if (phoneNumber == null) {phoneNumber = userOld.get().getPhone_number();}
        if (favoriteGenres == null) {favoriteGenres = userOld.get().getFavorite_genres();}
        if (gender == null) {gender = userOld.get().getGender();}
        try {
            mongoRepository.updateUser(username, name, surname, country, phoneNumber, favoriteGenres, gender);
            neoRepository.updateUser(username, name, surname, country, favoriteGenres);
        } catch (Exception e) {
            mongoRepository.updateUser(username,  userOld.get().getName(), userOld.get().getSurname(), userOld.get().getCountry(), userOld.get().getPhone_number(), userOld.get().getFavorite_genres(), userOld.get().getGender());
            throw new RuntimeException(e);
        }
    }

    public UserMongoDB getUserByUsername(String username) {
        Optional<UserMongoDB> user = mongoRepository.findById(username);
        if (user.isEmpty()) {throw new NoSuchElementException("User " + username + " not found");}
        return user.get();
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public void removeFromWatchlist(String username, String movieId) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!mongoRepository.isMovieInWatchlist(username, movieId)) {
            throw new IllegalArgumentException("Movie '" + movieId + "' is not in the watchlist");
        }
        try {
            mongoRepository.removeFromWatchlist(username,movieId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Slice<UserMovie> getLikedMovies(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.getLikedMovies(username,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
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

    @Transactional
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

    public Slice<UserCelebrity> getFollowedCelebrities(String username,int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.getFollowedCelebrities(username,pageable);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void addFollowedCelebrity(String username, int celebrityId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!celebrityNeoRepository.existsById(String.valueOf(celebrityId))) {
            throw new NoSuchElementException("Celebrity '" + celebrityId + "' doesn't exists");
        } else if (neoRepository.isCelebrityFollowed(username, String.valueOf(celebrityId))) {
            throw new IllegalArgumentException("Celebrity '" + celebrityId + "' is already followed");
        }
        try {
            neoRepository.addToFollowedCelebrities(username,String.valueOf(celebrityId));
            mongoRepository.addToFollowedCelebrities(username, celebrityId);
            celebrityMongoRepository.increaseFollowers(celebrityId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void removeFollowedCelebrity(String username, int celebrityId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!celebrityNeoRepository.existsById(String.valueOf(celebrityId))) {
            throw new NoSuchElementException("Celebrity '" + celebrityId + "' doesn't exists");
        } else if (!neoRepository.isCelebrityFollowed(username, String.valueOf(celebrityId))) {
            throw new IllegalArgumentException("Celebrity '" + celebrityId + "' isn't followed");
        }
        try {
            List<UserCelebrity> newCelebrities = neoRepository.removeFromFollowedCelebrities(username,String.valueOf(celebrityId));
            mongoRepository.removeFromFollowedCelebrities(username,newCelebrities);
            celebrityMongoRepository.decreaseFollowers(celebrityId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Slice<UserNeo4J> getFollowedUsers(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.getFollowed(username,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Slice<UserNeo4J> getFollowersUsers(String username,int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.getFollowers(username,pageable);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void addFollowedUser(String username, String followedUsername) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!neoRepository.existsById(followedUsername)) {
            throw new NoSuchElementException("User '" + followedUsername + "' doesn't exists");
        } else if (neoRepository.isUserFollowed(username, followedUsername)) {
            throw new IllegalArgumentException("User '" + followedUsername + "' is already followed");
        } else if (username.equals(followedUsername)) {
            throw new IllegalArgumentException("Users can't follow itself");
        }
        try {
            neoRepository.followUser(username,followedUsername);
            mongoRepository.increaseFollowed(username);
            mongoRepository.increaseFollowers(followedUsername);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void removeFollowedUser(String username, String followedUsername) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } else if (!neoRepository.existsById(String.valueOf(followedUsername))) {
            throw new NoSuchElementException("User '" + followedUsername + "' doesn't exists");
        } else if (!neoRepository.isUserFollowed(username, followedUsername)) {
            throw new IllegalArgumentException("User '" + followedUsername + "' isn't followed");
        }
        try {
            neoRepository.unfollowUser(username,followedUsername);
            mongoRepository.decreaseFollowed(username);
            mongoRepository.decreaseFollowers(followedUsername);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<UserMongoDB> searchUser(String query,String country,int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return mongoRepository.searchUser(query,country,(int)pageable.getOffset(),size);
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

    public Slice<MovieRecommendationsDTO> recommendMovie(String username,int page,int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendMovies(username,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Slice<CelebrityRecommendationsDTO> recommendCelebrity(String username,int page,int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendCelebrities(username,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Slice<UserRecommendationsDTO> recommendUser(String username,int page,int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendUsers(username,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Slice<MovieRecommendationsDTO> recommendCast(String username,int page,int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendByCast(username,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Slice<MovieRecommendationsDTO> recommendReview(String username,int page,int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("User '" + username + "' doesn't exists");
        } try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendByReview(username,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> findDifference(List<String> a, List<String> b) {
        Set<String> firstSet = new HashSet<>(a);

        List<String> result = new ArrayList<>();

        for (String mongoElement : b) {
            if (!firstSet.contains(mongoElement)) {
                result.add(mongoElement);
            }
        }

        return result;
    }

    public List<String> inconsistenciesNeo() {
        List<String> mongoDb=mongoRepository.findAllMovieIds().getAllIds();
        List<String> neo4j=neoRepository.findAllUsernames();
        return findDifference(neo4j, mongoDb);
    }
    public List<String> inconsistenciesMongo() {
        List<String> mongoDb=mongoRepository.findAllMovieIds().getAllIds();
        List<String> neo4j=neoRepository.findAllUsernames();
        return findDifference(mongoDb, neo4j);
    }

}
