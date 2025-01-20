package it.unipi.movieland.controller.User;

import it.unipi.movieland.model.User.User;
import it.unipi.movieland.model.User.LikedMovie;
import it.unipi.movieland.model.User.WatchlistItem;
import it.unipi.movieland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Crea un nuovo utente
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Ottieni un utente per ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    // Aggiungi un film alla lista dei preferiti dell'utente
    @PostMapping("/{userId}/likedMovies")
    public User addLikedMovie(@PathVariable String userId, @RequestBody LikedMovie likedMovie) {
        return userService.addLikedMovie(userId, likedMovie);
    }

    // Aggiungi un film alla watchlist dell'utente
    @PostMapping("/{userId}/watchlist")
    public User addWatchlistItem(@PathVariable String userId, @RequestBody WatchlistItem watchlistItem) {
        return userService.addWatchlistItem(userId, watchlistItem);
    }

    // Altri metodi per gestire l'utente
}
