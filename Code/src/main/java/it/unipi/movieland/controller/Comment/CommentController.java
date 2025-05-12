package it.unipi.movieland.controller.Comment;

import it.unipi.movieland.dto.Celebrity.CommentDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.exception.CommentNotFoundInMongoException;
import it.unipi.movieland.exception.PostNotFoundInMongoException;
import it.unipi.movieland.exception.UserNotFoundInMongoException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import it.unipi.movieland.model.Comment.CommentMongoDB;
import it.unipi.movieland.service.Comment.CommentService;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //CREATE COMMENT
    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestParam String text,
            @RequestParam String authorId,
            @RequestParam String postId) {

        try {
            CommentMongoDB comment = commentService.createComment(text, authorId, postId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "COMMENT CREATED SUCCESSFULLY");
            response.put("comment", comment);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommentNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "FAILED TO CREATE COMMENT", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO CREATE COMMENT", "details", e.getMessage()));
        }
    }

    //RETRIEVE COMMENT BY ID
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentById(
            @PathVariable String commentId) {
        try {
            CommentMongoDB comment = commentService.getCommentById(commentId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "COMMENT RETRIEVED SUCCESSFULLY");
            response.put("comment", comment);

            return ResponseEntity.ok(response);
        } catch (CommentNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "COMMENT NOT FOUND", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO RETRIEVE COMMENT", "details", e.getMessage()));
        }
    }

    //RETRIEVE ALL COMMENTS WITH PAGINATION
    @GetMapping("/")
    public Page<CommentMongoDB> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return commentService.getAllComments(page, size);
    }

    //DELETE COMMENT BY ID
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable String commentId) {
        try {
            commentService.deleteComment(commentId);

            return ResponseEntity.ok(Map.of("message", "COMMENT DELETED SUCCESSFULLY"));
        } catch (CommentNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "COMMENT NOT FOUND", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO DELETE COMMENT", "details", e.getMessage()));
        }
    }

    //UPDATE COMMENT BY ID
    @PutMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(
            @PathVariable String commentId,
            @RequestParam String text) {

        try {
            CommentMongoDB updatedComment = commentService.updateComment(commentId, text);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "POST UPDATED SUCCESSFULLY.");
            response.put("post", updatedComment);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);

        } catch (CommentNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "COMMENT NOT FOUND", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO UPDATE COMMENT", "details", e.getMessage()));
        }
    }

    //RETRIEVE COMMENTS BY AUTHOR ID WITH PAGINATION
    @GetMapping("/user/{authorId}")
    public ResponseEntity<?> getCommentsByAuthor(
            @PathVariable String authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<CommentMongoDB> comments = commentService.getCommentsByAuthor(authorId, page, size);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "COMMENTS RETRIEVED SUCCESSFULLY.", "comments", comments));

        } catch (UserNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "AUTHOR NOT FOUND", "details", e.getMessage()));
        } catch (CommentNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "COMMENT NOT FOUND", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO RETRIEVE COMMENTS", "details", e.getMessage()));
        }
    }

    //RETRIEVE COMMENTS BY DATE RANGE
    @GetMapping("/byDateRange")
    public ResponseEntity<Map<String, Object>> getCommentsByDateRange(
            @Parameter(description = "Start date in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,

            @Parameter(description = "End date in format 'yyyy-MM-ddTHH:mm:ss'")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<CommentDTO> posts = commentService.getCommentsByDateRange(startDate, endDate, page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "COMMENTS RETRIEVED SUCCESSFULLY.");
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

    //RETRIEVE COMMENTS BY POST ID
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPostId(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            ObjectId objectId = new ObjectId(postId);

            Slice<CommentMongoDB> comments = commentService.getCommentsByPostId(objectId, page, size);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "COMMENTS RETRIEVED SUCCESSFULLY.", "comments", comments));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "INVALID POST ID", "details", e.getMessage()));
        } catch (PostNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "POST NOT FOUND", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO RETRIEVE COMMENTS", "details", e.getMessage()));
        }
    }
}

