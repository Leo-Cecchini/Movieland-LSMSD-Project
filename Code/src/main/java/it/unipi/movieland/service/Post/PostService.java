package it.unipi.movieland.service.Post;

import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;
import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.repository.Post.Post_mongoDB_interface;
import it.unipi.movieland.repository.Post.Post_mongoDB_repo;
import it.unipi.movieland.service.exception.BusinessException;
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
    private Post_mongoDB_repo postRepository;

    @Autowired
    private Post_mongoDB_interface post_mongoDB_interface;

    // Get post by id
    public Optional<Post> getPostById(String id) {
        return post_mongoDB_interface.findById(id);
    }

    // Add new post
    public Post addPost(Post post) {
        return post_mongoDB_interface.save(post);
    }

    // Get posts by movieId
    public Page<Post> getPostsByMovieId(String movie_id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return post_mongoDB_interface.findbyMovieId(movie_id, pageRequest);
    }

    // Delete post
    public void deletePost(String id) {
        post_mongoDB_interface.deleteById(id);
    }

    public List<PostActivityDTO> getPostActivity() throws BusinessException {
        return postRepository.getPostActivity();
    }

    public List<UserInfluencerDTO> getInfluencersReport() throws BusinessException {
        return postRepository.getInfluencersReport();
    }

    public Page<Post> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return post_mongoDB_interface.findByDatetimeBetween(startDate, endDate, pageRequest);
    }
}