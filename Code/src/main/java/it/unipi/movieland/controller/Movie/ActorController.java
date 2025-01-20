package applicationMovieland.controller.Movie;

import applicationMovieland.model.Movie.Actor;
import applicationMovieland.service.Movie.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    // Endpoint per ottenere tutti gli attori
    @GetMapping
    public List<Actor> getAllActors() {
        return actorService.getAllActors();
    }

    // Endpoint per ottenere un attore tramite id
    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable int id) {
        Optional<Actor> actor = actorService.getActorById(id);
        return actor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere un nuovo attore
    @PostMapping
    public ResponseEntity<Actor> addActor(@RequestBody Actor actor) {
        Actor addedActor = actorService.addActor(actor);
        return ResponseEntity.ok(addedActor);
    }

    // Endpoint per aggiornare un attore
    @PutMapping("/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable int id, @RequestBody Actor actor) {
        actor.setId(id); // Imposta l'id dell'attore da aggiornare
        Actor updatedActor = actorService.updateActor(actor);
        return ResponseEntity.ok(updatedActor);
    }

    // Endpoint per eliminare un attore tramite id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable int id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }
}