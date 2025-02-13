package it.unipi.movieland.repository.Comment;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;
import it.unipi.movieland.model.Comment.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@Repository
public interface CommentMongoDBRepository extends MongoRepository<Comment, String> {

    //METODO PER CERCARE I COMMENTI IN BASE ALL'AUTORE
    Page<Comment> findByAuthor(String author, Pageable pageable);

    //METODO PER CERCARE I COMMENTI IN BASE AD UN INTERVALLO DI DATE
    Page<Comment> findByDatetimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Metodo per trovare i commenti tramite Aggregation
    @Aggregation(pipeline = {
            "{ $match: { post_id: ?0 } }",
            "{ $sort: { datetime: -1 } }",  // Ordina per data (opzionale, puoi aggiungere altri ordini)
    })
    Slice<Comment> findCommentsByPostId(ObjectId postId, Pageable pageable);
}


