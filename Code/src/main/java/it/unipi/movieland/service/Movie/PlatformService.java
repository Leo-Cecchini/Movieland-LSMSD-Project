package applicationMovieland.service.Movie;

import applicationMovieland.model.Movie.Platform;
import applicationMovieland.repository.Movie.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    // Ottieni tutte le piattaforme
    public List<Platform> getAllPlatforms() {
        return platformRepository.getAllPlatforms();
    }

    // Ottieni una piattaforma tramite ID
    public Optional<Platform> getPlatformById(String platformId) {
        return platformRepository.getPlatformById(platformId);
    }

    // Aggiungi una nuova piattaforma
    public Platform addPlatform(Platform platform) {
        return platformRepository.addPlatform(platform);
    }

    // Modifica una piattaforma esistente
    public Platform updatePlatform(String platformId, Platform updatedPlatform) {
        return platformRepository.updatePlatform(platformId, updatedPlatform);
    }

    // Elimina una piattaforma tramite ID
    public void deletePlatform(String platformId) {
        platformRepository.deletePlatform(platformId);
    }
}