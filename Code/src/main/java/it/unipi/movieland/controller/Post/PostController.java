package it.unipi.movieland.controller.Post;

import io.swagger.v3.oas.annotations.Parameter;
import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;
import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.service.Post.PostService;
import it.unipi.movieland.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // Get all post by movie_id
    @GetMapping("/movie/{movie_id}")
    public Page<PostDTO> getPostsByMovieId(
            @PathVariable String movie_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return postService.getPostsByMovieId(movie_id, page, size);
    }

    // Get post by id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        Optional<Post> post = postService.getPostById(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add new post
    @PostMapping
    public ResponseEntity<Post> addPost(
            @RequestParam String movie_id,
            @RequestParam String author,
            @RequestParam String text,
            @RequestParam LocalDateTime date) {
        Post newPost = new Post(date, text, author, movie_id,null);
        Post addedPost = postService.addPost(newPost);
        return ResponseEntity.ok(addedPost);
    }

    // Delete post by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    //activty report
    @GetMapping("/activityReport")
    public ResponseEntity<List<PostActivityDTO>> getActivityReport() throws BusinessException {
        List<PostActivityDTO> activity = postService.getPostActivity();
        return ResponseEntity.ok(activity);
    }

    //inlfuencer users
    @GetMapping("/influencersReport")
    public ResponseEntity<List<UserInfluencerDTO>> getInfluencersReport() throws BusinessException {
        List<UserInfluencerDTO> influencers = postService.getInfluencersReport();
        return ResponseEntity.ok(influencers);
    }

    // Get posts with date range
    @GetMapping("/byDateRange")
    public Page<PostDTO> getCommentsByDateRange(
            @Parameter(description = "Start data in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam String startDate,

            @Parameter(description = "End data in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam String endDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        return postService.getCommentsByDateRange(start, end, page, size);
    }


}