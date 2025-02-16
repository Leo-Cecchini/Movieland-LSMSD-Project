package it.unipi.movieland.service.Post;

import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;
import it.unipi.movieland.model.Comment.Comment;
import it.unipi.movieland.model.Movie.Movie;
import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.repository.Post.PostMongoDBRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final UserMongoDBRepository userRepository;
    private final PostMongoDBRepository postMongoDBRepository;
    private final MovieMongoDBRepository movieMongoDBRepository;


    @Autowired
    public PostService(PostMongoDBRepository postMongoDBRepository, UserMongoDBRepository userRepository, MovieMongoDBRepository movieMongoDBRepository) {

        this.userRepository = userRepository;
        this.postMongoDBRepository = postMongoDBRepository;
        this.movieMongoDBRepository = movieMongoDBRepository;
    }

    //METHOD TO CREATE A POST
    public Post createPost(String text, String author, String movieId) {
        if (!userRepository.existsById(author)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AUTHOR WITH ID : " + author + " NOT FOUND.");}

        if(!movieMongoDBRepository.existsById(movieId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MOVIE WITH ID " + movieId + " NOT FOUND");}

        Post post = new Post(text,author,movieId,null);
        return postMongoDBRepository.save(post);
    }

    //METHOD TO UPDATE AN EXISTING COMMENT
    public Post updatePost(String id, String text) {

        Post existingPost = postMongoDBRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "POST WITH ID " + id + " NOT FOUND"));

        existingPost.setText(text);
        existingPost.setDatetime(LocalDateTime.now());

        return postMongoDBRepository.save(existingPost);
    }


    // Get post by id
    public Optional<Post> getPostById(String id) {
        return postMongoDBRepository.findById(id);
    }

    // Add new post
    public Post addPost(Post post) {
        return postMongoDBRepository.save(post);
    }


    // Get posts by movieId
    public Page<PostDTO> getPostsByMovieId(String movie_id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return postMongoDBRepository.findByMovieId(movie_id, pageRequest);
    }

    // Delete post
    public void deletePost(String id) {
        postMongoDBRepository.deleteById(id);
    }


    public List<PostActivityDTO> getPostActivity() {
        return postMongoDBRepository.getPostActivity();
    }

    public List<UserInfluencerDTO> getInfluencersReport()  {
        return postMongoDBRepository.getInfluencersReport();
    }

    public Page<PostDTO> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return postMongoDBRepository.findByDatetimeBetween(startDate, endDate, pageRequest);
    }
}