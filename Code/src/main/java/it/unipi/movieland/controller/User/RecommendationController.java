package it.unipi.movieland.controller.User;

import it.unipi.movieland.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users/recommendation")
public class RecommendationController {

    @Autowired
    private UserService userService;

    //GET CELEBRITY RECOMMENDATIONS FOR A SPECIFIC USER WITH PAGINATION
    @GetMapping("/{username}/celebrities")
    public ResponseEntity<?> getCelebrityRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendCelebrity(username, page, size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET USER RECOMMENDATIONS BASED ON A SPECIFIC USER WITH PAGINATION (USING NEO4J)
    @GetMapping("/{username}/users")
    public ResponseEntity<?> getUserRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendUser(username, page, size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET MOVIE RECOMMENDATIONS BASED ON A SPECIFIC USER WITH PAGINATION (USING NEO4J)
    @GetMapping("/{username}/movies")
    public ResponseEntity<?> getMovieRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendMovie(username, page, size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET MOVIE RECOMMENDATIONS BASED ON GENRE FOR A SPECIFIC USER (USING MONGODB)
    @GetMapping("/{username}/movies/byGenre")
    public ResponseEntity<?> getGenreRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendByGenre(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET MOVIE RECOMMENDATIONS BASED ON CAST FOR A SPECIFIC USER
    @GetMapping("/{username}/movies/byCast")
    public ResponseEntity<?> getCastRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendCast(username, page, size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET MOVIE RECOMMENDATIONS BASED ON REVIEWS FOR A SPECIFIC USER
    @GetMapping("/{username}/movies/byReviews")
    public ResponseEntity<?> getReviewRecommendation(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            return new ResponseEntity<>(userService.recommendReview(username, page, size), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}