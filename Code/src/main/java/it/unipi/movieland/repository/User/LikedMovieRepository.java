package it.unipi.movieland.repository.User;

import it.unipi.movieland.model.User.LikedMovie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikedMovieRepository extends MongoRepository<LikedMovie, String> {
    // Query personalizzate
}
