package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.Keyword;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;

@Repository
public class KeywordRepository {

    private List<Keyword> keywords = new ArrayList<>();

    // Recupera tutte le keyword
    public List<Keyword> getAllKeywords() {
        return keywords;
    }

    // Aggiungi una nuova lista di keyword
    public Keyword addKeyword(Keyword keyword) {
        keywords.add(keyword);
        return keyword;
    }

    // Puoi aggiungere metodi per cercare, rimuovere, o aggiornare le keyword, se necessario
}