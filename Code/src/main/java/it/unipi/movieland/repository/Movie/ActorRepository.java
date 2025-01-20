package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.model.Movie.Actor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends MongoRepository<Actor, Integer> {

    // Puoi aggiungere metodi personalizzati per gestire gli attori
    // Ad esempio, per trovare un attore per nome:
    // List<Actor> findByName(String name);
}