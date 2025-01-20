package applicationMovieland.repository.Movie;

import applicationMovieland.model.Movie.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

    // Puoi aggiungere metodi personalizzati per gestire gli attori
    // Ad esempio, per trovare un attore per nome:
    // List<Actor> findByName(String name);
}