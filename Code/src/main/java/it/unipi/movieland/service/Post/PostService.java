package applicationMovieland.service.Post;

import applicationMovieland.model.Post.Post;
import applicationMovieland.repository.Post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Aggiungi un nuovo post
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    // Ottieni tutti i post
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Ottieni i post per un determinato movieId
    public List<Post> getPostsByMovieId(String movieId) {
        return postRepository.findByMovieId(movieId);
    }

    // Ottieni un singolo post per id
    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    // Elimina un post
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }
}