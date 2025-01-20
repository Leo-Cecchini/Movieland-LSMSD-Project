package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.Seasons;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SeasonsRepository {

    private List<Seasons> seasonsList = new ArrayList<>();

    // Ottieni tutte le stagioni
    public List<Seasons> getAllSeasons() {
        return seasonsList;
    }

    // Ottieni le stagioni per ID del film/serie
    public Optional<Seasons> getSeasonsById(String movieId) {
        return seasonsList.stream()
                .filter(seasons -> seasons.getSeasonList().contains(movieId)) // esempio di logica di filtro
                .findFirst();
    }

    // Aggiungi nuove stagioni
    public Seasons addSeasons(Seasons seasons) {
        seasonsList.add(seasons);
        return seasons;
    }

    // Modifica una stagione esistente
    public Seasons updateSeasons(String movieId, Seasons updatedSeasons) {
        Seasons existingSeasons = getSeasonsById(movieId)
                .orElseThrow(() -> new RuntimeException("Seasons not found"));
        existingSeasons.setSeasonList(updatedSeasons.getSeasonList());
        return existingSeasons;
    }

    // Elimina una stagione
    public void deleteSeasons(String movieId) {
        Seasons seasons = getSeasonsById(movieId)
                .orElseThrow(() -> new RuntimeException("Seasons not found"));
        seasonsList.remove(seasons);
    }
}