package it.unipi.movieland.repository.User;

import it.unipi.movieland.model.User.FollowedCelebrity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FollowedCelebrityRepository extends MongoRepository<FollowedCelebrity, Integer> {
    // Query personalizzate
}