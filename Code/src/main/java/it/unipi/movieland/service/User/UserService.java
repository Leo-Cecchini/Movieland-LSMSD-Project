package it.unipi.movieland.service.User;


import it.unipi.movieland.model.User.LikedMovie;
import it.unipi.movieland.model.User.User;
import it.unipi.movieland.model.User.WatchlistItem;
import it.unipi.movieland.repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Crea un nuovo User
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Ottieni un utente per ID
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // Aggiorna i dettagli dell'utente
    public User updateUser(String id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(updatedUser.getEmail());
            user.setName(updatedUser.getName());
            user.setSurname(updatedUser.getSurname());
            // Aggiungi altre proprietà da aggiornare
            return userRepository.save(user);
        }
        return null;
    }

    // Aggiungi un film alla lista dei preferiti dell'utente
    public User addLikedMovie(String userId, LikedMovie likedMovie) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getLikedMovies().add(likedMovie);
            return userRepository.save(user);
        }
        return null;
    }

    // Aggiungi un film alla watchlist dell'utente
    public User addWatchlistItem(String userId, WatchlistItem watchlistItem) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getWatchlist().add(watchlistItem);
            return userRepository.save(user);
        }
        return null;
    }

    // Altri metodi per la logica dell'utente
}
