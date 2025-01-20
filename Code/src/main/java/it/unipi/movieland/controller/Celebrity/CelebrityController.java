package it.unipi.movieland.controller.Celebrity;

import it.unipi.movieland.model.Celebrity.Celebrity;
import it.unipi.movieland.service.Celebrity.CelebrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/celebrities")
public class CelebrityController {

    @Autowired
    private CelebrityService celebrityService;

    // Endpoint per ottenere tutte le celebrities
    @GetMapping
    public List<Celebrity> getAllCelebrities() {
        return celebrityService.getAllCelebrities();
    }

    // Endpoint per ottenere una celebrity per ID
    @GetMapping("/{id}")
    public ResponseEntity<Celebrity> getCelebrityById(@PathVariable int id) {
        Optional<Celebrity> celebrity = celebrityService.getCelebrityById(id);
        return celebrity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere una nuova celebrity
    @PostMapping
    public ResponseEntity<Celebrity> addCelebrity(@RequestBody Celebrity celebrity) {
        Celebrity addedCelebrity = celebrityService.addCelebrity(celebrity);
        return ResponseEntity.ok(addedCelebrity);
    }

    // Endpoint per aggiornare una celebrity esistente
    @PutMapping("/{id}")
    public ResponseEntity<Celebrity> updateCelebrity(@PathVariable int id, @RequestBody Celebrity updatedCelebrity) {
        Celebrity updated = celebrityService.updateCelebrity(id, updatedCelebrity);
        return ResponseEntity.ok(updated);
    }

    // Endpoint per eliminare una celebrity
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCelebrity(@PathVariable int id) {
        celebrityService.deleteCelebrity(id);
        return ResponseEntity.noContent().build();
    }
}