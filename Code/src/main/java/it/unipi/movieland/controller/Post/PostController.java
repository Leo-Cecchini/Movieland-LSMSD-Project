package it.unipi.movieland.controller.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unipi.movieland.exception.PostNotFoundInMongoException;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;
import it.unipi.movieland.model.Post.PostMongoDB;
import it.unipi.movieland.service.Post.PostService;
import it.unipi.movieland.service.exception.BusinessException;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    //CREATE POST
    @PostMapping
    public ResponseEntity<Object> createPost(
            @RequestParam String text,
            @RequestParam String authorId,
            @RequestParam String movieId) {

        try {
            PostMongoDB post = postService.createPost(text, authorId, movieId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "POST CREATED SUCCESSFULLY");
            response.put("post", post);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (PostNotFoundInMongoException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "FAILED TO CREATE POST", "details", e.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO CREATE POST", "details", e.getMessage()));
        }
    }

    //UPDATE A POST BY ID
    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePost(
            @PathVariable String postId,
            @RequestParam String text) {

        try {
            PostMongoDB updatedPost = postService.updatePost(postId, text);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "POST UPDATED SUCCESSFULLY.");
            response.put("post", updatedPost);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);

        } catch (PostNotFoundInMongoException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "POST NOT FOUND", "details", e.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO UPDATE POST", "details", e.getMessage()));
        }
    }

    //DELETE A POST BY ID
    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, String>> deletePost(
            @PathVariable String postId) {

        try {
            postService.deletePost(postId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "POST WITH ID " + postId + " DELETED SUCCESSFULLY.");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (PostNotFoundInMongoException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "POST NOT FOUND");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "FAILED TO DELETE POST");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //GET A POST BY ID
    @GetMapping("/{postId}")
    public ResponseEntity<Object> getPostById(
            @PathVariable String postId) {

        try {
            PostMongoDB getPostById = postService.getPostById(postId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "POST RETRIEVED SUCCESSFULLY.");
            response.put("post", getPostById);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);

        } catch (PostNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "POST NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET POST", "details", e.getMessage()));  // Messaggio corretto
        }
    }

    //GET POSTS BY AUTHOR WITH PAGINATION
    @GetMapping("/author/{userId}")
    public ResponseEntity<Object> getPostByAuthor(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<PostMongoDB> posts = postService.getPostByAuthor(userId, page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "POSTS RETRIEVED SUCCESSFULLY.");
            response.put("posts", posts);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);

        } catch (PostNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "POSTS NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET POSTS", "details", e.getMessage()));
        }
    }

    //GET ALL POSTS BY MOVIE ID
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Object> getPostsByMovieId(
            @PathVariable String movieId) {
        try {
            List<PostDTO> posts = postService.getPostsByMovieId(movieId);

            if (posts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "MOVIE ID NOT FOUND", "details", "No posts found for movieId: " + movieId));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "POSTS RETRIEVED SUCCESSFULLY.");
            response.put("posts", posts);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET POSTS", "details", e.getMessage()));
        }
    }

    //SEARCH POSTS WITHIN A DATE RANGE
    @GetMapping("/byDateRange")
    public ResponseEntity<Map<String, Object>> getPostsByDateRange(
            @Parameter(description = "Start date in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam String startDate,

            @Parameter(description = "End date in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam String endDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);

            Page<PostDTO> posts = postService.getPostsByDateRange(start, end, page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "POSTS RETRIEVED SUCCESSFULLY.");
            response.put("posts", posts);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);

        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "INVALID DATE FORMAT", "details", "DATE SHOULD BE IN THE FORMAT 'yyyy-MM-dd'T'HH:mm:ss'."));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET POSTS", "details", e.getMessage()));
        }
    }

    //GET POST ACTIVITY REPORT
    @GetMapping("/activityReport")
    public ResponseEntity<List<PostActivityDTO>> getActivityReport() throws BusinessException {
        List<PostActivityDTO> activity = postService.getPostActivity();
        return ResponseEntity.ok(activity);
    }

    //GET INFLUENCERS REPORT
    @GetMapping("/influencersReport")
    public ResponseEntity<List<UserInfluencerDTO>> getInfluencersReport() throws BusinessException {
        List<UserInfluencerDTO> influencers = postService.getInfluencersReport();
        return ResponseEntity.ok(influencers);
    }
}