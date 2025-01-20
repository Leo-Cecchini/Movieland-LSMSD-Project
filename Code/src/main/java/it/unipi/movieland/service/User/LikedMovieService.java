package it.unipi.movieland.service.User;

import it.unipi.movieland.model.User.LikedMovie;
import it.unipi.movieland.repository.User.LikedMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikedMovieService {

    @Autowired
    private LikedMovieRepository likedMovieRepository;

    // Aggiungi un film alla lista dei film preferiti
    public LikedMovie addLikedMovie(LikedMovie movie) {
        return likedMovieRepository.save(movie);
    }

    // Ottieni tutti i film preferiti
    public List<LikedMovie> getAllLikedMovies() {
        return likedMovieRepository.findAll();
    }

    // Ottieni un film preferito tramite filmId
    public Optional<LikedMovie> getLikedMovieById(String filmId) {
        return likedMovieRepository.findById(filmId);
    }

    // Elimina un film preferito tramite filmId
    public void deleteLikedMovie(String filmId) {
        likedMovieRepository.deleteById(filmId);
    }

    // Puoi aggiungere ulteriori metodi per altre operazioni sui film preferiti
}