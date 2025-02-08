package it.unipi.movieland.repository.Celebrity;

import it.unipi.movieland.model.Celebrity.CelebrityMongoDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.mongodb.repository.Aggregation;
import java.util.List;

//
@Repository
public interface CelebrityMongoDBRepository extends MongoRepository<CelebrityMongoDB, Integer> {

    void deleteById(int id);

    Optional<CelebrityMongoDB> findById(int id);

    //FIND ACTORS BY NAME OR CHARACTERS
    @Aggregation(pipeline = {
            "{ $unwind: '$jobs' }",
            "{ $match: { $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'jobs.character': { $regex: ?0, $options: 'i' } } ] } }",
            "{ $group: { _id: '$_id', name: { $first: '$name' }, jobs: { $push: '$jobs' }, followers: { $first: '$followers' }, poster: { $first: '$poster' } } }",
            "{ $limit: 10 }"
        })

    List<CelebrityMongoDB> searchActorsAndCharacters(String searchTerm);
}
