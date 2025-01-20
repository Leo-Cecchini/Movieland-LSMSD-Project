package applicationMovieland.repository.Movie;

import applicationMovieland.model.Movie.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public class GenreRepository {

    private List<Genre> genres = new ArrayList<>();

    // Recupera tutti i generi
    public List<Genre> getAllGenres() {
        return genres;
    }

    // Aggiungi un nuovo genere
    public Genre addGenre(Genre genre) {
        genres.add(genre);
        return genre;
    }

    // Puoi aggiungere metodi per cercare, rimuovere, o aggiornare i generi, se necessario
}