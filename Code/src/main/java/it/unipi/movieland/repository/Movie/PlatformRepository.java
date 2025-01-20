package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.Platform;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PlatformRepository {

    private List<Platform> platforms = new ArrayList<>();

    // Ottieni tutte le piattaforme
    public List<Platform> getAllPlatforms() {
        return platforms;
    }

    // Ottieni una piattaforma tramite ID
    public Optional<Platform> getPlatformById(String platformId) {
        return platforms.stream().filter(platform -> platform.getPlatformList().contains(platformId)).findFirst();
    }

    // Aggiungi una nuova piattaforma
    public Platform addPlatform(Platform platform) {
        platforms.add(platform);
        return platform;
    }

    // Modifica una piattaforma esistente
    public Platform updatePlatform(String platformId, Platform updatedPlatform) {
        Platform existingPlatform = getPlatformById(platformId).orElseThrow(() -> new RuntimeException("Platform not found"));
        existingPlatform.setPlatformList(updatedPlatform.getPlatformList());
        return existingPlatform;
    }

    // Elimina una piattaforma tramite ID
    public void deletePlatform(String platformId) {
        Platform platform = getPlatformById(platformId).orElseThrow(() -> new RuntimeException("Platform not found"));
        platforms.remove(platform);
    }
}