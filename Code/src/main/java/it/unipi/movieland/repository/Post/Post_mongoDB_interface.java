package it.unipi.movieland.repository.Post;

import it.unipi.movieland.model.Post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface Post_mongoDB_interface extends MongoRepository<Post, String> {
    @Query("{ 'movie_id': ?0 }")
    List<Post> findbyMovieId(String movie_id);

    Page<Post> findByDatetimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}