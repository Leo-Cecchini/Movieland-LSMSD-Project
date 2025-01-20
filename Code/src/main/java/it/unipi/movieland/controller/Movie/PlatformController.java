package applicationMovieland.controller.Movie;

import applicationMovieland.model.Movie.Platform;
import applicationMovieland.service.Movie.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/platforms")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    // Endpoint per ottenere tutte le piattaforme
    @GetMapping
    public List<Platform> getAllPlatforms() {
        return platformService.getAllPlatforms();
    }

    // Endpoint per ottenere una piattaforma tramite ID
    @GetMapping("/{platformId}")
    public ResponseEntity<Platform> getPlatformById(@PathVariable String platformId) {
        Optional<Platform> platform = platformService.getPlatformById(platformId);
        return platform.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere una nuova piattaforma
    @PostMapping
    public ResponseEntity<Platform> addPlatform(@RequestBody Platform platform) {
        Platform addedPlatform = platformService.addPlatform(platform);
        return ResponseEntity.ok(addedPlatform);
    }

    // Endpoint per aggiornare una piattaforma esistente
    @PutMapping("/{platformId}")
    public ResponseEntity<Platform> updatePlatform(@PathVariable String platformId, @RequestBody Platform updatedPlatform) {
        Platform platform = platformService.updatePlatform(platformId, updatedPlatform);
        return ResponseEntity.ok(platform);
    }

    // Endpoint per eliminare una piattaforma tramite ID
    @DeleteMapping("/{platformId}")
    public ResponseEntity<Void> deletePlatform(@PathVariable String platformId) {
        platformService.deletePlatform(platformId);
        return ResponseEntity.noContent().build();
    }
}