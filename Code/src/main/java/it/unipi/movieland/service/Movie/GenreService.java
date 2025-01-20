package applicationMovieland.service.Movie;

import applicationMovieland.model.Movie.Genre;
import applicationMovieland.repository.Movie.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    // Ottieni tutti i generi
    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    // Aggiungi un nuovo genere
    public Genre addGenre(Genre genre) {
        return genreRepository.addGenre(genre);
    }

    // Puoi aggiungere metodi per la gestione dei generi, come rimuovere o modificare
}