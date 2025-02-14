package it.unipi.movieland.service.Post;

import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;
import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.repository.Post.PostMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostMongoDBRepository postMongoDBRepository;

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