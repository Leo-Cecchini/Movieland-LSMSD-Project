package it.unipi.movieland.controller.Movie;

import it.unipi.movieland.model.Movie.Keyword;
import it.unipi.movieland.service.Movie.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    // Endpoint per ottenere tutte le keyword
    @GetMapping
    public List<Keyword> getAllKeywords() {
        return keywordService.getAllKeywords();
    }

    // Endpoint per aggiungere una nuova lista di keyword
    @PostMapping
    public ResponseEntity<Keyword> addKeyword(@RequestBody Keyword keyword) {
        Keyword addedKeyword = keywordService.addKeyword(keyword);
        return ResponseEntity.ok(addedKeyword);
    }

    // Puoi aggiungere altri endpoint se necessario, per esempio per eliminare una keyword
}