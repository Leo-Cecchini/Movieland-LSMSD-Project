package it.unipi.movieland.repository.Manager;

import it.unipi.movieland.model.Manager.Manager;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ManagerRepository extends MongoRepository<Manager, String> {
    boolean existsByEmail(String email);
}
