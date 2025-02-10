package it.unipi.movieland.controller.User;

import it.unipi.movieland.model.GenreEnum;
import it.unipi.movieland.model.User.UserMongoDB;
import it.unipi.movieland.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(@RequestParam int page) {
        try {
            List<UserMongoDB> users = userService.getAllUsers(page);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addUser(String username, String email, String name, String surname, String password, String country, String phoneNumber, @RequestBody List<GenreEnum> favoriteGenres, String gender, LocalDate birthday) {
        try {
            UserMongoDB user = userService.addUser(username, email, name, surname, password, country, phoneNumber, favoriteGenres, gender, birthday);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(String username, String password) {
        try {;
             return new ResponseEntity<>(userService.authenticate(username,password),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserMongoDB user = userService.getUserByUsername(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, String email, String name, String surname, String country, String phoneNumber, @RequestBody List<GenreEnum> favoriteGenres, String gender) {
        try {
            userService.updateUser(username, email, name, surname, country, phoneNumber, favoriteGenres, gender);
            return new ResponseEntity<>("User updated",HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/watchlist")
    public ResponseEntity<?> getWatchlist(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.getWatchlist(username),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{username}/watchlist/{id}")
    public ResponseEntity<String> addToWatchlist(@PathVariable String username, @PathVariable String id) {
        try {
            userService.addToWatchlist(username, id);
            return new ResponseEntity<>("Watchlist updated", HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{username}/watchlist/{id}")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable String username, @PathVariable String id) {
        try {
            userService.removeFromWatchlist(username, id);
            return new ResponseEntity<>("Watchlist updated", HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/likedMovies")
    public ResponseEntity<?> getLikedMovies(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.getLikedMovies(username),HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{username}/likedMovies/{id}")
    public ResponseEntity<String> addLikedMovie(@PathVariable String username, @PathVariable String id) {
        try {
            userService.addLikedMovie(username, id);
            return new ResponseEntity<>("Liked movies updated", HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{username}/likedMovies/{id}")
    public ResponseEntity<String> removeLikedMovie(@PathVariable String username, @PathVariable String id) {
        try {
            userService.removeLikedMovie(username, id);
            return new ResponseEntity<>("Liked movies updated", HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/followedCelebrities")
    public ResponseEntity<?> getFollowedCelebrities(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.getFollowedCelebrities(username),HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }  catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{username}/followedCelebrities/{id}")
    public ResponseEntity<String> addFollowedCelebrity(@PathVariable String username, @PathVariable int id) {
        try {
            userService.addFollowedCelebrity(username, id);
            return new ResponseEntity<>("Followed Celebrities updated", HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{username}/followedCelebrities/{id}")
    public ResponseEntity<String> removeFollowedCelebrity(@PathVariable String username, @PathVariable int id) {
        try {
            userService.removeFollowedCelebrity(username, id);
            return new ResponseEntity<>("Followed Celebrities updated", HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/followedUsers")
    public ResponseEntity<?> getFollowedUsers(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.getFollowedUsers(username),HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/followedUsers/self")
    public ResponseEntity<?> getFollowers(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.getFollowersUsers(username),HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{username}/followedUsers/{id}")
    public ResponseEntity<String> addFollowedUser(@PathVariable String username, @PathVariable String id) {
        try {
            userService.addFollowedUser(username, id);
            return new ResponseEntity<>("Followed users updated", HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{username}/followedUsers/{id}")
    public ResponseEntity<String> removeFollowedUser(@PathVariable String username, @PathVariable String id) {
        try {
            userService.removeFollowedUser(username, id);
            return new ResponseEntity<>("Followed users updated", HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query,@RequestParam(required = false) String country) {
        try {
             List<UserMongoDB> users = userService.searchUser(query,country);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (RuntimeException e){
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
