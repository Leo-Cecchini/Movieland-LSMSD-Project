package applicationMovieland.repository.Celebrity;

import applicationMovieland.model.Celebrity.Celebrity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CelebrityRepository {

    private List<Celebrity> celebrityList = new ArrayList<>();

    // Ottieni tutte le celebrities
    public List<Celebrity> getAllCelebrities() {
        return celebrityList;
    }

    // Ottieni una celebrity per ID
    public Optional<Celebrity> getCelebrityById(int id) {
        return celebrityList.stream()
                .filter(celebrity -> celebrity.getId() == id)
                .findFirst();
    }

    // Aggiungi una nuova celebrity
    public Celebrity addCelebrity(Celebrity celebrity) {
        celebrityList.add(celebrity);
        return celebrity;
    }

    // Modifica una celebrity esistente
    public Celebrity updateCelebrity(int id, Celebrity updatedCelebrity) {
        Celebrity existingCelebrity = getCelebrityById(id)
                .orElseThrow(() -> new RuntimeException("Celebrity not found"));
        existingCelebrity.setName(updatedCelebrity.getName());
        existingCelebrity.setJobs(updatedCelebrity.getJobs());
        existingCelebrity.setFollowers(updatedCelebrity.getFollowers());
        existingCelebrity.setPoster(updatedCelebrity.getPoster());
        return existingCelebrity;
    }

    // Elimina una celebrity
    public void deleteCelebrity(int id) {
        Celebrity celebrity = getCelebrityById(id)
                .orElseThrow(() -> new RuntimeException("Celebrity not found"));
        celebrityList.remove(celebrity);
    }
}