package it.unipi.movieland.service.User;

import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenderEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.User.UserCelebrity;
import it.unipi.movieland.model.User.UserMovie;
import it.unipi.movieland.model.User.UserMongoDB;
import it.unipi.movieland.model.User.UserNeo4J;
import it.unipi.movieland.repository.Celebrity.CelebrityMongoDBRepository;
import it.unipi.movieland.repository.Celebrity.CelebrityNeo4JRepository;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.repository.Movie.MovieNeo4JRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import it.unipi.movieland.repository.User.UserNeo4JRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class UserService {

    @Autowired
    private UserMongoDBRepository mongoRepository;

    @Autowired
    private UserNeo4JRepository neoRepository;

    @Autowired
    private MovieMongoDBRepository movieMongoRepository;

    @Autowired
    private MovieNeo4JRepository movieNeoRepository;

    @Autowired
    private CelebrityMongoDBRepository celebrityMongoRepository;

    @Autowired
    private CelebrityNeo4JRepository celebrityNeoRepository;

    //METHOD TO ENCRYPT A STRING USING AES WITH IV
    public static String encrypt(String str, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(encryptedBytes);
    }

    //METHOD TO GET ALL USERS WITH PAGINATION
    public List<UserMongoDB> getAllUsers(int page,int size) {
        if (page < 0) {throw new IllegalArgumentException("PAGE MUST BE A POSITIVE INTEGER.");}
        try {
            return mongoRepository.findAll(PageRequest.of(page, size)).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO GET A USER BY USERNAME
    public UserMongoDB getUserByUsername(String username) {
        Optional<UserMongoDB> user = mongoRepository.findById(username);
        if (user.isEmpty()) {throw new NoSuchElementException("USER " + username + " NOT FOUND.");}
        return user.get();
    }

    //METHOD TO ADD A NEW USER
    @Transactional
    public UserMongoDB addUser(String username, String email, String name, String surname, String password, CountryEnum country, String phoneNumber, List<GenreEnum> favoriteGenres, GenderEnum gender, LocalDate birthday) {
        if (mongoRepository.existsById(username) || neoRepository.existsById(username)) {
            throw new IllegalArgumentException("USER WITH USERNAME '" + username + "' ALREADY EXISTS.");
        } else if (mongoRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("EMAIL'" + email + "' ALREADY USED.");
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

    //METHOD TO UPDATE AN EXISTING USER'S DETAILS, CHECKING EMAIL DUPLICATION
    @Transactional
    public void modifyUserDetails(
            String username, String email, String name, String surname, CountryEnum country,
            String phoneNumber, List<GenreEnum> favoriteGenres, GenderEnum gender) {

        Optional<UserMongoDB> existingUser = mongoRepository.findById(username);
        if (existingUser.isEmpty()) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }

        UserMongoDB user = existingUser.get();

        if (email != null && !email.equals(user.getEmail()) && mongoRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("EMAIL '" + email + "' IS ALREADY USED BY ANOTHER ACCOUNT.");
        }

        email = (email != null) ? email : user.getEmail();
        name = (name != null) ? name : user.getName();
        surname = (surname != null) ? surname : user.getSurname();
        country = (country != null) ? country : user.getCountry();
        phoneNumber = (phoneNumber != null) ? phoneNumber : user.getPhoneNumber();
        favoriteGenres = (favoriteGenres != null) ? favoriteGenres : user.getFavoriteGenres();
        gender = (gender != null) ? gender : user.getGender();

        try {
            mongoRepository.modifyUserDetails(username, email, name, surname, country, phoneNumber, favoriteGenres, gender);
            neoRepository.modifyUserDetails(username, email, name, surname, country, favoriteGenres);
        } catch (Exception e) {

            mongoRepository.modifyUserDetails(username, user.getEmail(), user.getName(), user.getSurname(), user.getCountry(),
                    user.getPhoneNumber(), user.getFavoriteGenres(), user.getGender());
            throw new RuntimeException("ERROR UPDATING USER DETAILS.", e);
        }
    }

    //METHOD TO DELETE A USER FROM BOTH MONGODB AND NEO4J
    @Transactional
    public void deleteUser(String username) {
        if (!mongoRepository.existsById(username) && !neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        UserMongoDB userOld =mongoRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS."));
        try {
            mongoRepository.deleteById(username);
            neoRepository.deleteById(username);
        } catch (Exception e) {
            mongoRepository.save(userOld);
            throw new RuntimeException(e);
        }
    }

    //METHOD TO AUTHENTICATE A USER BY COMPARING PASSWORDS
    public boolean authenticate(String username, String password) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS.");
        }
        try {
            String pass=mongoRepository.findById(username).get().getPassword();
            String passwordE=encrypt(password,"MovieLand0123456");
            return pass.equals(passwordE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO GET THE WATCHLIST OF A USER
    public WatchlistDTO getWatchlist(String username) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS.");
        }
        try {
            UserMongoDB user= mongoRepository.getWatchlist(username);
            return new WatchlistDTO(user.getWatchlist());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO ADD A MOVIE TO THE USER'S WATCHLIST
    @Transactional
    public void addToWatchlist(String username, String movieId) {
        Optional<UserMongoDB> user = mongoRepository.findById(username);
        if (user.isEmpty()) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS.");
        } else if (user.get().getWatchlist().size()>20) {
            throw new IllegalArgumentException("CAN'T HAVE MORE THAN 20 MOVIES IN THE WATCHLIST");
        } else if  (mongoRepository.isMovieInWatchlist(username, movieId)) {
            throw new IllegalArgumentException("MOVIE '" + movieId + "' IS ALREADY IN THE WATCHLIST.");
        } else if (!movieMongoRepository.existsById(movieId)) {
            throw new NoSuchElementException("MOVIE '" + movieId + "' DOESN'T EXISTS");
        }
        try {
            mongoRepository.addToWatchlist(username,movieId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO REMOVE A MOVIE FROM A USER'S WATCHLIST
    @Transactional
    public void removeFromWatchlist(String username, String movieId) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS.");
        } else if (!mongoRepository.isMovieInWatchlist(username, movieId)) {
            throw new IllegalArgumentException("MOVIE '" + movieId + "' IS NOT IN THE WATCHLIST.");
        }
        try {
            mongoRepository.removeFromWatchlist(username,movieId);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //METHOD TO GET A USER'S LIKED MOVIES WITH PAGINATION
    public Slice<UserMovie> getLikedMovies(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS.");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.getLikedMovies(username,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO ADD A MOVIE TO A USER'S LIKED MOVIES
    @Transactional
    public void addLikedMovie(String username, String movieId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS.");
        } else if (!movieNeoRepository.existsById(movieId)) {
            throw new NoSuchElementException("MOVIE '" + movieId + "' DOESN'T EXISTS.");
        } else if (neoRepository.isMovieLiked(username, movieId)) {
            throw new IllegalArgumentException("MOVIE '" + movieId + "' IS ALREADY LIKED.");
        }
        try {
            neoRepository.addToLikedMovies(username,movieId);
            mongoRepository.addToLikedMovies(username, movieId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO REMOVE A MOVIE FROM A USER'S LIKED MOVIES
    @Transactional
    public void removeLikedMovie(String username, String movieId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        } else if (!movieNeoRepository.existsById(movieId)) {
            throw new NoSuchElementException("MOVIE '" + movieId + "' DOESN'T EXISTS");
        } else if (!neoRepository.isMovieLiked(username, movieId)) {
            throw new IllegalArgumentException("MOVIE '" + movieId + "' ISN'T LIKED");
        }
        try {
            List<UserMovie> newMovies = neoRepository.removeFromLikedMovies(username, movieId);
            mongoRepository.removeFromLikedMovies(username, newMovies);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO GET A USER'S FOLLOWED CELEBRITIES WITH PAGINATION
    public Slice<UserCelebrity> getFollowedCelebrities(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.getFollowedCelebrities(username, pageable);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //METHOD TO ADD A CELEBRITY TO A USER'S FOLLOWED CELEBRITIES
    @Transactional
    public void addFollowedCelebrity(String username, int celebrityId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        } else if (!celebrityNeoRepository.existsById(String.valueOf(celebrityId))) {
            throw new NoSuchElementException("CELEBRITY '" + celebrityId + "' DOESN'T EXISTS");
        } else if (neoRepository.isCelebrityFollowed(username, String.valueOf(celebrityId))) {
            throw new IllegalArgumentException("CELEBRITY '" + celebrityId + "' IS ALREADY FOLLOWED");
        }
        try {
            neoRepository.addToFollowedCelebrities(username, String.valueOf(celebrityId));
            mongoRepository.addToFollowedCelebrities(username, celebrityId);
            celebrityMongoRepository.increaseFollowers(celebrityId);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //METHOD TO REMOVE A CELEBRITY FROM A USER'S FOLLOWED CELEBRITIES
    @Transactional
    public void removeFollowedCelebrity(String username, int celebrityId) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        } else if (!celebrityNeoRepository.existsById(String.valueOf(celebrityId))) {
            throw new NoSuchElementException("CELEBRITY '" + celebrityId + "' DOESN'T EXISTS");
        } else if (!neoRepository.isCelebrityFollowed(username, String.valueOf(celebrityId))) {
            throw new IllegalArgumentException("CELEBRITY '" + celebrityId + "' ISN'T FOLLOWED");
        }
        try {
            List<UserCelebrity> newCelebrities = neoRepository.removeFromFollowedCelebrities(username, String.valueOf(celebrityId));
            mongoRepository.removeFromFollowedCelebrities(username, newCelebrities);
            celebrityMongoRepository.decreaseFollowers(celebrityId);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //METHOD TO GET A USER'S FOLLOWED USERS WITH PAGINATION
    public Slice<UserNeo4J> getFollowedUsers(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.getFollowed(username, pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO GET A USER'S FOLLOWERS WITH PAGINATION
    public Slice<UserNeo4J> getFollowersUsers(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.getFollowers(username, pageable);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //METHOD TO ADD A USER TO A USER'S FOLLOWED USERS
    @Transactional
    public void addFollowedUser(String username, String followedUsername) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        } else if (!neoRepository.existsById(followedUsername)) {
            throw new NoSuchElementException("USER '" + followedUsername + "' DOESN'T EXISTS");
        } else if (neoRepository.isUserFollowed(username, followedUsername)) {
            throw new IllegalArgumentException("USER '" + followedUsername + "' IS ALREADY FOLLOWED");
        } else if (username.equals(followedUsername)) {
            throw new IllegalArgumentException("USERS CAN'T FOLLOW ITSELF");
        }
        try {
            neoRepository.followUser(username, followedUsername);
            mongoRepository.increaseFollowed(username);
            mongoRepository.increaseFollowers(followedUsername);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //METHOD TO REMOVE A USER FROM A USER'S FOLLOWED USERS
    @Transactional
    public void removeFollowedUser(String username, String followedUsername) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        } else if (!neoRepository.existsById(String.valueOf(followedUsername))) {
            throw new NoSuchElementException("USER '" + followedUsername + "' DOESN'T EXISTS");
        } else if (!neoRepository.isUserFollowed(username, followedUsername)) {
            throw new IllegalArgumentException("USER '" + followedUsername + "' ISN'T FOLLOWED");
        }
        try {
            neoRepository.unfollowUser(username, followedUsername);
            mongoRepository.decreaseFollowed(username);
            mongoRepository.decreaseFollowers(followedUsername);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //METHOD TO SEARCH FOR A USER BASED ON QUERY AND COUNTRY WITH PAGINATION
    public List<UserMongoDB> searchUsers(String query, String country, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return mongoRepository.searchUsers(query, country, (int) pageable.getOffset(), size);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO RECOMMEND MOVIES BASED ON GENRE FOR A USER
    public GenreRecommendationsDTO recommendByGenre(String username) {
        if (!mongoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            return mongoRepository.recommendedMoviesGenre(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO RECOMMEND MOVIES FOR A USER WITH PAGINATION
    public Slice<MovieRecommendationsDTO> recommendMovie(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendMovies(username, pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO RECOMMEND CELEBRITIES FOR A USER WITH PAGINATION
    public Slice<CelebrityRecommendationsDTO> recommendCelebrity(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendCelebrities(username, pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO RECOMMEND USERS FOR A USER WITH PAGINATION
    public Slice<UserRecommendationsDTO> recommendUser(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendUsers(username, pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO RECOMMEND MOVIES BASED ON CAST FOR A USER WITH PAGINATION
    public Slice<MovieRecommendationsDTO> recommendCast(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendByCast(username, pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO RECOMMEND MOVIES BASED ON REVIEWS FOR A USER WITH PAGINATION
    public Slice<MovieRecommendationsDTO> recommendReview(String username, int page, int size) {
        if (!neoRepository.existsById(username)) {
            throw new NoSuchElementException("USER '" + username + "' DOESN'T EXISTS");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return neoRepository.recommendByReview(username, pageable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //METHOD TO FIND THE DIFFERENCE BETWEEN TWO LISTS OF STRINGS
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

    //METHOD TO FIND INCONSISTENCIES BETWEEN NEO4J AND MONGODB (MOVIE IDS NOT IN MONGODB)
    public List<String> inconsistenciesNeo() {
        List<String> mongoDb = mongoRepository.findAllMovieIds().getAllIds();
        List<String> neo4j = neoRepository.findAllUsernames();
        return findDifference(neo4j, mongoDb);
    }

    //METHOD TO FIND INCONSISTENCIES BETWEEN MONGODB AND NEO4J (MOVIE IDS NOT IN NEO4J)
    public List<String> inconsistenciesMongo() {
        List<String> mongoDb = mongoRepository.findAllMovieIds().getAllIds();
        List<String> neo4j = neoRepository.findAllUsernames();
        return findDifference(mongoDb, neo4j);
    }
}
