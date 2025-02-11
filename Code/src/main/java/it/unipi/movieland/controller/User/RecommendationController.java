package it.unipi.movieland.controller.User;


import it.unipi.movieland.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}/celebrity")
    public ResponseEntity<?> getCelebrityRecommendation(@PathVariable String username) {
        try {
             return new ResponseEntity<>(userService.recommendCelebrity(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Raccomandazione utenti (Neo4j)
    @GetMapping("/{username}/user")
    public ResponseEntity<?> getUserRecommendation(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.recommendUser(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Raccomandazione film (Neo4j)
    @GetMapping("/{username}/movie")
    public ResponseEntity<?> getMovieRecommendation(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.recommendMovie(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Raccomandazione per genere (MongoDb)
    @GetMapping("/{username}/movie/genre")
    public ResponseEntity<?> getGenreRecommendation(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.recommendByGenre(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/movie/cast")
    public ResponseEntity<?> getCastRecommendation(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.recommendCast(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/movie/review")
    public ResponseEntity<?> getReviewRecommendation(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.recommendReview(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
