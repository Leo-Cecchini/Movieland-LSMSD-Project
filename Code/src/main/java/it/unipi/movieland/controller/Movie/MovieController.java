package it.unipi.movieland.controller.Movie;

import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.CountryEnum;
import it.unipi.movieland.model.GenreEnum;
import it.unipi.movieland.model.PlatformEnum;
import it.unipi.movieland.model.TitleTypeEnum;
import it.unipi.movieland.service.Movie.*;
import it.unipi.movieland.model.Movie.Movie;
import it.unipi.movieland.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Search movie by id
    @GetMapping("/{movieId}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String movieId) {
        Optional<Movie> movie = movieService.getMovieById(movieId);
        return movie.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Search title by word
    @GetMapping("/search/titleOrKeyword")
    public ResponseEntity<ResponseWrapper<Page<SearchTitleDTO>>> getMovieByTitle(
            @RequestParam("type") TitleTypeEnum type,
            @RequestParam String label,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SearchTitleDTO> movie = movieService.getMovieByTitleOrKeyword(type, label, page, size);
            if(movie.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Titles fetched successfully, " +
                        "but no titles found", movie));
            return ResponseEntity.ok(new ResponseWrapper<>("Titles fetched successfully", movie));
        }catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching title: " + e.getMessage(), null));
        }
    }

    //search with filters
    @GetMapping("/search/withFilters")
    public ResponseEntity<ResponseWrapper<Page<SearchTitleDTO>>> getMovieByTitleOrKeywordWithFilters(
            @RequestParam("type") TitleTypeEnum type,
            @RequestParam("label") Optional<String> label,
            @RequestParam("genre") Optional<List<GenreEnum>> genre,
            @RequestParam("release_year") Optional<Integer> release_year,
            @RequestParam("platform") Optional<PlatformEnum> platform,
            @RequestParam("production_countries") Optional<CountryEnum> production_countries,
            @RequestParam("age_certification") Optional<String> age_certification,
            @RequestParam("imdb_votes") Optional<Integer> imdb_votes,
            @RequestParam("imdb_scores") Optional<Integer> imdb_scores,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        try {
            Page<SearchTitleDTO> movie = movieService.getTitleWithFilters(type, label, genre, release_year, platform, production_countries, age_certification, imdb_scores, imdb_votes, page, size);
            if(movie.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Titles fetched successfully, " +
                        "but no titles found", movie));
            return ResponseEntity.ok(new ResponseWrapper<>("Titles fetched successfully", movie));
        }catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching titles: " + e.getMessage(), null));
        }
    }

    // Get all movie reviews
    /*@GetMapping("/allReviews/{movieId}")
    public ResponseEntity<List<Review>> getAllReviewsByMovieId(@PathVariable String movieId) {
        Optional<List<Review>> review = movieService.getReviewsByMovieId(movieId);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }*/

    // Search new title by name (and by year...released before a specific year)
    @GetMapping("/searchNew/movieIdByName")
    public ResponseEntity<ResponseWrapper<List<SearchNewTitleDTO>>> searchNewTitleByName(
            @RequestParam("type") TitleTypeEnum type,
            @RequestParam("title") String title,
            @RequestParam("year") Optional<Integer> year
            ){
        try {

            Map<Integer, List<SearchNewTitleDTO>> movies = movieService.searchNewTitleByName(type, title, year);
            Integer result = movies.keySet().stream().findFirst().orElse(null);
            return switch(result){
                case 0 -> ResponseEntity.ok(new ResponseWrapper<>("Titles fetched successfully", movies.get(0)));
                case 1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(type + " with title " + title + " not found", movies.get(1)));
                default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>("An error occurred with the api ", null));
            };
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching titles: " + e.getMessage(), null));
        }
    }

    //Add title by id
    @PostMapping("/addMovieById")
    public ResponseEntity<ResponseWrapper<Integer>> addTitleById(
            @RequestParam TitleTypeEnum type,
            @RequestParam String id) {
        try {
            int result = movieService.addTitleById(type, id);
            return switch (result) {
                case 0 -> ResponseEntity.ok(new ResponseWrapper<>(type + " added successfully", result));
                case 1 ->
                        ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(type + " with id: " + id + " already exists in the database", null));
                case 2 ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(type + " with id " + id + " not found", null));
                default ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>("Error adding title: " + id, null));
            };
        }catch (IOException | InterruptedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching titles: " + e.getMessage(), null));
        }
    }

    // Update title: general info
    @PutMapping("/{movieId}")
    public ResponseEntity<Object> updateMovie(
            @PathVariable String movieId,
            @RequestBody UpdateTitleDTO movie
    ) {
        boolean isUpdated = movieService.updateMovie(movieId, movie);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Title updated successfully in both MongoDB and Neo4j"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Title with movieId " + movieId + " not found in one or both databases"));
        }
    }

    // Delete title
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }

    // Add title actor
    @PostMapping("/{movieId}/addActor/{actorId}")
    public ResponseEntity<String> addRole(@PathVariable String movieId, @PathVariable Integer actor_id, @RequestParam String name,
                                          @RequestParam String character) {
        int result = movieService.addRole(movieId, actor_id, name, character);
        return switch (result) {
            case 0 -> ResponseEntity.ok("Role: '"+ character +"' for actor: "+ actor_id +" added successfully in title: "+movieId);
            case 1 -> ResponseEntity.status(HttpStatus.CONFLICT).body("Actor already exists or no update was made");
            default -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actor with id "+ movieId +" not found!");
        };
    }

    // Update title actor
    @PutMapping("/{movieId}/updateActor/{actorId}")
    public ResponseEntity<String> updateRole(@PathVariable String movieId, @PathVariable Integer actorId, @RequestParam String name,
                                            @RequestParam String character) {
        int result = movieService.updateRole(movieId, actorId, name, character);
        if(result == 0)
            return ResponseEntity.ok("Role '"+ character +"' for actor: "+ actorId +" updated successfully in title: "+movieId);
        else
            return ResponseEntity.ok("Title with id "+ movieId +" not found!");
    }

    // Remove title actor
    @DeleteMapping("/{movieId}/removeRole/{actorId}")
    public ResponseEntity<String> deleteRole(@PathVariable String movieId, @PathVariable Integer actorId) {
        int result = movieService.deleteRole(movieId, actorId);
        if( result == 0 )
            return ResponseEntity.ok("Role for actor: "+ actorId +" deleted successfully in title: "+movieId);
        else
            return  ResponseEntity.ok("Title with id "+ movieId +" not found!");
    }


    // Add title director
    @PostMapping("/{movieId}/addDirector/{directorId}")
    public ResponseEntity<String> addDirector(@PathVariable String movieId, @PathVariable Integer directorId, @RequestParam String name) {
        int result = movieService.addDirector(movieId, directorId, name);
        return switch (result) {
            case 0 -> ResponseEntity.ok("Director "+ directorId +" added successfully to title "+ movieId);
            case 1 -> ResponseEntity.ok("Director with id: "+ directorId +" already exists or no update was made");
            default -> ResponseEntity.ok("Title with id "+ movieId +" not found!");
        };
    }
    // Remove title director
    @PutMapping("/{movieId}/updateDirector/{directorId}")
    public ResponseEntity<String> updateRole(@PathVariable String movieId, @PathVariable Integer directorId, @RequestParam String name) {
        int result = movieService.updateDirector(movieId, directorId, name);
        if ( result == 0 )
            return ResponseEntity.ok("Director "+ directorId +" of title "+ movieId +" updated  successfully!");
        else
            return ResponseEntity.ok("Title with id "+ movieId +" not found!");
    }
    // Update title director
    @DeleteMapping("/{movieId}/removeDirector/{directorId}")
    public ResponseEntity<String> deleteDirector(@PathVariable String movieId, @PathVariable Integer directorId) {
        int result = movieService.deleteDirector(movieId, directorId);
        if ( result == 0 )
            return ResponseEntity.ok("Director "+ directorId +" of title "+ movieId +" removed successfully!");
        else
            return ResponseEntity.ok("Title with id "+ movieId +" not found!");
    }
}