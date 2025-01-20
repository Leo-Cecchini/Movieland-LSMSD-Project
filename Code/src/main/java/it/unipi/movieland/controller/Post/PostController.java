package it.unipi.movieland.controller.Post;

import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.service.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // Endpoint per ottenere tutti i post
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Endpoint per ottenere i post per un determinato movieId
    @GetMapping("/movie/{movieId}")
    public List<Post> getPostsByMovieId(@PathVariable String movieId) {
        return postService.getPostsByMovieId(movieId);
    }

    // Endpoint per ottenere un singolo post per id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Optional<Post> post = postService.getPostById(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere un nuovo post
    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        Post addedPost = postService.addPost(post);
        return ResponseEntity.ok(addedPost);
    }

    // Endpoint per eliminare un post tramite id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}