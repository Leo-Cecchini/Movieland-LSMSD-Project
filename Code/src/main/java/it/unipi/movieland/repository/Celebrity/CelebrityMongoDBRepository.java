package it.unipi.movieland.repository.Celebrity;

import it.unipi.movieland.dto.ListIdDTO;
import it.unipi.movieland.model.Celebrity.CelebrityMongoDB;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CelebrityMongoDBRepository extends MongoRepository<CelebrityMongoDB, Integer> {

    //DELETE CELEBRITY BY ID
    void deleteById(int id);

    //FIND CELEBRITY BY ID
    Optional<CelebrityMongoDB> findById(int id);

    //FIND ACTORS BY NAME OR CHARACTER
    @Aggregation(pipeline = {
            "{ $match: { $text: { $search: '?0' } } }",
            "{ $addFields: { score: { $meta: 'textScore' } } }",
            "{ $unwind: '$jobs' }",
            "{ $group: { " +
                    "_id: '$_id', " + "name: { $first: '$name' }, " + "jobs: { $push: '$jobs' }, " +
                    "followers: { $first: '$followers' }, " + "poster: { $first: '$poster' }, " +
                    "score: { $first: '$score' }, " + "imdb_score: { $first: '$imdb_score' } } }",
            "{ $sort: { score: -1, imdb_score: -1 } }",
            "{ $project: { name: 1, jobs: 1, followers: 1, poster: 1 } }",
            "{ $limit: 10 }"
    })
    List<CelebrityMongoDB> searchActorsAndCharacters(String searchTerm);

    //INCREASE FOLLERWERS BY 1
    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'followers': 1 } }")
    void increaseFollowers(int celebrityId);

    //DECREASE FOLLOWERS BY 1
    @Query("{ '_id': ?0 }")
    @Update("{ $inc: { 'followers': -1 } }")
    void decreaseFollowers(int celebrityId);

    //FIND ALL CELEBRITY IDS
    @Aggregation(pipeline = {
            "{ $project: { _id: 1 } }",
            "{ $group: { _id: 1, allIds: { $push: '$_id' } } }"
    })
    ListIdDTO findAllIds();
}