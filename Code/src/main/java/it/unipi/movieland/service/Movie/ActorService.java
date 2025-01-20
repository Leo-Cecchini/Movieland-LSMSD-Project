package applicationMovieland.service.Movie;

import applicationMovieland.model.Movie.Actor;
import applicationMovieland.repository.Movie.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    // Ottieni tutti gli attori
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    // Ottieni un attore tramite id
    public Optional<Actor> getActorById(int id) {
        return actorRepository.findById(id);
    }

    // Aggiungi un attore
    public Actor addActor(Actor actor) {
        return actorRepository.save(actor);
    }

    // Modifica un attore
    public Actor updateActor(Actor actor) {
        return actorRepository.save(actor);
    }

    // Elimina un attore tramite id
    public void deleteActor(int id) {
        actorRepository.deleteById(id);
    }
}
