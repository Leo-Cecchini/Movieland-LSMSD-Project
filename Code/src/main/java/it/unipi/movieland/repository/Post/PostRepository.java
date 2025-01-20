package it.unipi.movieland.repository.Post;

import it.unipi.movieland.model.Post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    // Puoi aggiungere metodi personalizzati, ad esempio per ottenere i post per movieId
    List<Post> findByMovieId(String movieId);

    // Aggiungi altri metodi personalizzati se necessario
}