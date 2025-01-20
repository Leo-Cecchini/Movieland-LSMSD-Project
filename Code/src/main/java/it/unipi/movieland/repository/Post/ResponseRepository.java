package it.unipi.movieland.repository.Post;

import it.unipi.movieland.model.Post.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends MongoRepository<Response, String> {

    // Puoi aggiungere metodi personalizzati per gestire le risposte
}
