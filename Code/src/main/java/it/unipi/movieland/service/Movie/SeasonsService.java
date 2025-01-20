package applicationMovieland.service.Movie;

import applicationMovieland.model.Movie.Seasons;
import applicationMovieland.repository.Movie.SeasonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonsService {

    @Autowired
    private SeasonsRepository seasonsRepository;

    // Ottieni tutte le stagioni
    public List<Seasons> getAllSeasons() {
        return seasonsRepository.getAllSeasons();
    }

    // Ottieni le stagioni per un film/serie tramite ID
    public Optional<Seasons> getSeasonsById(String movieId) {
        return seasonsRepository.getSeasonsById(movieId);
    }

    // Aggiungi nuove stagioni
    public Seasons addSeasons(Seasons seasons) {
        return seasonsRepository.addSeasons(seasons);
    }

    // Modifica una stagione esistente
    public Seasons updateSeasons(String movieId, Seasons updatedSeasons) {
        return seasonsRepository.updateSeasons(movieId, updatedSeasons);
    }

    // Elimina una stagione
    public void deleteSeasons(String movieId) {
        seasonsRepository.deleteSeasons(movieId);
    }
}