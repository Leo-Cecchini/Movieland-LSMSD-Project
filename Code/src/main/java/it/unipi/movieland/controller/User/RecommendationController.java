package it.unipi.movieland.controller.User;


import it.unipi.movieland.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}/celebrity")
    public ResponseEntity<?> getCelebrityRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
             return new ResponseEntity<>(userService.recommendCelebrity(username,page,size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Raccomandazione utenti (Neo4j)
    @GetMapping("/{username}/user")
    public ResponseEntity<?> getUserRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendUser(username,page,size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Raccomandazione film (Neo4j)
    @GetMapping("/{username}/movie")
    public ResponseEntity<?> getMovieRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendMovie(username,page,size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Raccomandazione per genere (MongoDb)
    @GetMapping("/{username}/movie/genre")
    public ResponseEntity<?> getGenreRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendByGenre(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/movie/cast")
    public ResponseEntity<?> getCastRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendCast(username,page,size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/movie/review")
    public ResponseEntity<?> getReviewRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendReview(username,page,size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
