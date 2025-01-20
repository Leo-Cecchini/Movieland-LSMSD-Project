package applicationMovieland.service.Movie;

import applicationMovieland.model.Movie.Keyword;
import applicationMovieland.repository.Movie.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    // Ottieni tutte le keyword
    public List<Keyword> getAllKeywords() {
        return keywordRepository.getAllKeywords();
    }

    // Aggiungi una nuova lista di keyword
    public Keyword addKeyword(Keyword keyword) {
        return keywordRepository.addKeyword(keyword);
    }

    // Puoi aggiungere metodi per la gestione delle keyword, come rimuovere o modificare
}