package it.unipi.movieland.repository.User;

import it.unipi.movieland.model.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Puoi aggiungere query personalizzate se necessario
}
