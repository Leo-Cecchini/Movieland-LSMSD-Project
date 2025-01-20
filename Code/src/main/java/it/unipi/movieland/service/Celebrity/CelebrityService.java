package it.unipi.movieland.service.Celebrity;

import it.unipi.movieland.model.Celebrity.Celebrity;
import it.unipi.movieland.repository.Celebrity.CelebrityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CelebrityService {

    @Autowired
    private CelebrityRepository celebrityRepository;

    // Ottieni tutte le celebrities
    public List<Celebrity> getAllCelebrities() {
        return celebrityRepository.getAllCelebrities();
    }

    // Ottieni una celebrity per ID
    public Optional<Celebrity> getCelebrityById(int id) {
        return celebrityRepository.getCelebrityById(id);
    }

    // Aggiungi una nuova celebrity
    public Celebrity addCelebrity(Celebrity celebrity) {
        return celebrityRepository.addCelebrity(celebrity);
    }

    // Modifica una celebrity esistente
    public Celebrity updateCelebrity(int id, Celebrity updatedCelebrity) {
        return celebrityRepository.updateCelebrity(id, updatedCelebrity);
    }

    // Elimina una celebrity
    public void deleteCelebrity(int id) {
        celebrityRepository.deleteCelebrity(id);
    }
}