package it.unipi.movieland.controller.Celebrity;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import it.unipi.movieland.model.Celebrity.CelebrityMongoDB;
import it.unipi.movieland.model.Celebrity.CelebrityNeo4J;
import it.unipi.movieland.service.Celebrity.CelebrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import it.unipi.movieland.model.Celebrity.Job;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@RestController
@RequestMapping("/api/celebrities")
public class CelebrityController {

    private final CelebrityService celebrityService;

    @Autowired
    public CelebrityController(CelebrityService celebrityService) {
        this.celebrityService = celebrityService;
    }

    //ENDPOINT PER RECUPERARE TUTTE LE CELEBRITY CON PAGINAZIONE (MONGODB)
    @GetMapping("/mongo")
    public Page<CelebrityMongoDB> getAllCelebritiesMongo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {  // Default: 100 risultati per pagina
        return celebrityService.getAllCelebritiesMongo(PageRequest.of(page, size));
    }

    //ENDPOINT PER RECUPERARE TUTTE LE CELEBRITA' CON PAGINAZIONE (NEO4J)
    @GetMapping("/neo4j")
    public ResponseEntity<Page<CelebrityNeo4J>> getAllCelebrities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CelebrityNeo4J> response = celebrityService.getAllCelebrities(page, size);
        return ResponseEntity.ok(response);
    }

    //ENDPOINT PER RECUPERARE UNA CELEBRITY PER PERSONID (NEO4J)
    @GetMapping("/neo4j/{personId}")
    public ResponseEntity<Object> getCelebrityById(@PathVariable("personId") String personId) {

        Optional<CelebrityNeo4J> celebrity = celebrityService.getCelebrityByIdNeo4j(personId);

        if (celebrity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Celebrity with ID " + personId + " not found in the database"));
        }
        return ResponseEntity.ok(celebrity.get());
    }

    //ENDPOINT PER TROVARE UNA CELEBRITY PER ID (MONGODB)
    @GetMapping("/mongo/{id}")
    public ResponseEntity<Object> getCelebrityByIdMongo(@PathVariable int id) {
        Optional<CelebrityMongoDB> celebrity = celebrityService.getCelebrityByIdMongo(id);

        if (celebrity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Celebrity with ID " + id + " not found in the MongoDB database"));
        }
        return ResponseEntity.ok(celebrity.get());
    }

    //ENDPOINT PER CREARE UNA CELEBRITY
    @PostMapping("/create")
    public ResponseEntity<Object> createCelebrity(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam String poster) {
        try {
            String responseMessage = celebrityService.createCelebrityInBothDatabases(id, name, poster);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", responseMessage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create celebrity", "details", e.getMessage()));
        }
    }

    //ENDPOINT PER ELIMINARE UN CELEBRITA'
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCelebrityById(@PathVariable("id") int id) {
        try {
            celebrityService.deleteCelebrityMongo(id);
            celebrityService.deleteCelebrityNeo4j(id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Celebrity with ID " + id + " has been deleted from both databases"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete celebrity from both databases", "details", e.getMessage()));
        }
    }

    //ENDPOINT PER AGGIUNGERE I JOB AD UN ACTOR
    @PostMapping("/{id}/jobs/actor")
    public ResponseEntity<Object> addJobToActor(
            @PathVariable int id,
            @RequestParam String movie_id,
            @RequestParam String movie_title,
            @RequestParam String character) {

        // Chiamata al service per aggiungere il lavoro
        boolean isJobAdded = celebrityService.addJobToActor(id, movie_id, movie_title, character);

        if (isJobAdded) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Job added successfully to actor with ID " + id));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to add job to actor"));
        }
    }

    //ENDPOINT PER AGGIUNGERE I JOB AD UN DIRECTOR
    @PostMapping("/{id}/jobs/director")  // Cambia il percorso da "/actor" a "/director"
    public ResponseEntity<Object> addJobToDirector(
            @PathVariable int id,
            @RequestParam String movie_id,
            @RequestParam String movie_title) {

        boolean isJobAdded = celebrityService.addJobToDirector(id, movie_id, movie_title);

        if (isJobAdded) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Job added successfully to director with ID " + id));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to add job to director"));
        }
    }

    //ENDPOINT PER ELIMINARE UN JOB AD UN DIRETTORE O ATTORE
    @DeleteMapping("/mongo/jobs/{jobId}")
    public ResponseEntity<Object> deleteJobById(@PathVariable String jobId) {
        boolean isJobRemoved = celebrityService.removeJobById(jobId);

        if (isJobRemoved) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)  // HTTP 204: No Content (successo senza corpo)
                    .body(Map.of("message", "Job with ID " + jobId + " removed successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)  // HTTP 404: Not Found
                    .body(Map.of("message", "Job with ID " + jobId + " not found"));
        }
    }


    //ENDPOINT FOR UPDATE THE CELEBRITY
    @PutMapping("/update/{personId}")
    public ResponseEntity<Object> updateCelebrity(
            @PathVariable String personId,
            @RequestParam String name,
            @RequestParam String poster) {

        boolean isUpdated = celebrityService.updateCelebrity(personId, name, poster);

        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Celebrity updated successfully in both MongoDB and Neo4j"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Celebrity with personId " + personId + " not found in one or both databases"));
        }
    }

    // ENDPOINT PER CERCARE UNA CELEBRITA' TRAMITE PAROLA CHIAVE O NOME (MONGODB)
    @GetMapping("/mongo/search")
    public List<CelebrityMongoDB> searchMongo(@RequestParam String query) {
        return celebrityService.searchActorsByCharacterMongo(query);
    }

    //DA VERIFICARE ENDPOINT PER RACCOMANDARE LA CELEBRITA' TRAMITE CONNESSIONI DI SECONDO GRADO (NEO4J)
    @GetMapping("/neo4j/recommend/actors/second-degree")
    public List<Map<String, Object>> getSecondDegreeCelebrityRecommendations(@RequestParam String username) {
        return celebrityService.getSecondDegreeCelebrityRecommendations(username);
    }
}