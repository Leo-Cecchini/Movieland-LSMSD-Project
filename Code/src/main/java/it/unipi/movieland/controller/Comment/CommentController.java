package it.unipi.movieland.controller.Comment;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDateTime;

import it.unipi.movieland.model.Comment.Comment;
import it.unipi.movieland.service.Comment.CommentService;
import it.unipi.movieland.repository.Post.PostMongoDBRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService, UserMongoDBRepository userRepository, PostMongoDBRepository postRepository)
    {
        this.commentService = commentService;
    }

    //ENDPOINT TO CREATE A COMMENT (MONGODB)
    @PostMapping
    public Comment createComment(
            @RequestParam String text,
            @RequestParam String authorId,
            @RequestParam String postId)
    {
        return commentService.createComment(text,authorId,postId);
    }

    //ENDPOINT TO RETRIEVE ALL COMMENTS (MONGODB)
    @GetMapping
    public Page<Comment> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size)
    {
        return commentService.getAllComments(page, size);
    }

    //ENDPOINT TO RETRIEVE A COMMENT BY ID (MONGODB)
    @GetMapping("/{id}")
    public Comment getCommentById(
            @PathVariable String id)
    {
        return commentService.getCommentById(id);
    }

    //ENDPOINT TO DELETE A COMMENT BY ID (MONGODB)
    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable String id)
    {
        commentService.deleteComment(id);
    }

    //ENDPOINT TO MODIFY A COMMENT BY ID (MONGODB)
    @PutMapping("/{id}")
    public Comment updateComment(
            @PathVariable String id,
            @RequestParam String text)
    {
        return commentService.updateComment(id, text);
    }

    //ENDPOINT TO GET COMMENTS BY AUTHOR WITH PAGINATION
    @GetMapping("/author/{author}")
    public Page<Comment> getCommentsByAuthor(
            @PathVariable String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        return commentService.getCommentsByAuthor(author, page, size);
    }

    //ENDPOINT TO SEARCH COMMENTS WITHIN A DATE RANGE
    @GetMapping("/byDateRange")
    public Page<Comment> getCommentsByDateRange(
            @Parameter(description = "Start data in formato 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,

            @Parameter(description = "End data in formato 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        return commentService.getCommentsByDateRange(startDate, endDate, page, size);
    }

    //ENDPOINT TO GET COMMENTS FOR A SPECIFIC POST
    @GetMapping("/post/{postId}")
    public Slice<Comment> getCommentsByPostId(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    )
    {
        ObjectId objectId = new ObjectId(postId);
        return commentService.getCommentsByPostId(objectId, page, size);
    }
}