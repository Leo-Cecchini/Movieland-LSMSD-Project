package it.unipi.movieland.controller.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Parameter;

import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;
import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.service.Post.PostService;
import it.unipi.movieland.service.exception.BusinessException;

    @RestController
    @RequestMapping("/posts")
    public class PostController {

    @Autowired
    private PostService postService;

    //ENDPOINT TO CREATE A POST
    @PostMapping
    public Post createPost(
            @RequestParam String text,
            @RequestParam String authorId,
            @RequestParam String movieId) {

        return postService.createPost(text,authorId,movieId);
    }

    //ENDPOINT TO MODIFY A COMMENT BY ID (MONGODB)
    @PutMapping("/{postId}")
    public Post updatePost(
            @PathVariable String postId,
            @RequestParam String text)
    {
        return postService.updatePost(postId, text);
    }

    //ENDPOINT TO RETRIEVE ALL POST BY MOVIE ID
    @GetMapping("/movie/{movieId}")
    public Page<PostDTO> getPostsByMovieId(
            @PathVariable String movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return postService.getPostsByMovieId(movieId, page, size);
    }

    //ENDPOINT TO RETRIEVE A POST BY ID
    @GetMapping("/{postId}")
    public Post getPostById(
            @PathVariable String postId)
    {
        return postService.getPostById(postId);
    }

    //ENDPOINT TO GET POST BY AUTHOR WITH PAGINATION
    @GetMapping("/author/{userId}")
    public Page<Post> getPostByAuthor(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        return postService.getPostByAuthor(userId, page, size);
    }

    //ENDPOINT TO DELETE A POST BY ID
    @DeleteMapping("/{postId}")
    public void deletePost(
            @PathVariable String postId)
    {
        postService.deletePost(postId);
    }

    //ENDPOINT TO SEARCH POSTS WITHIN A DATE RANGE
    @GetMapping("/byDateRange")
    public Page<PostDTO> getPostsByDateRange(
            @Parameter(description = "Start data in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam String startDate,

            @Parameter(description = "End data in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam String endDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        return postService.getPostsByDateRange(start, end, page, size);
        }

    //ENDPOINT
    @GetMapping("/activityReport")
    public ResponseEntity<List<PostActivityDTO>> getActivityReport() throws BusinessException {
        List<PostActivityDTO> activity = postService.getPostActivity();
        return ResponseEntity.ok(activity);
    }

    //ENDPOINT
    @GetMapping("/influencersReport")
    public ResponseEntity<List<UserInfluencerDTO>> getInfluencersReport() throws BusinessException {
        List<UserInfluencerDTO> influencers = postService.getInfluencersReport();
        return ResponseEntity.ok(influencers);
    }
}