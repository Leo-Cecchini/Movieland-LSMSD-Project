package it.unipi.movieland.controller.Movie;

import it.unipi.movieland.dto.*;
import it.unipi.movieland.exception.*;
import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.Enum.PlatformEnum;
import it.unipi.movieland.model.Enum.TitleTypeEnum;
import it.unipi.movieland.model.Movie.MovieMongoDB;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.service.Movie.*;
import it.unipi.movieland.service.exception.BusinessException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieMongoDBRepository movieMongoDBRepository;

    //GET ALL MOVIE WITH PAGINATION
    @GetMapping("/")
    public ResponseEntity<Page<MovieMongoDTO>> getAllMovies(
            @RequestParam(defaultValue = "0")   @Min(0) int page,
            @RequestParam(defaultValue = "10")  @Max(100) int size) {

        Page<MovieMongoDTO> result = movieService.getAllMovies(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    //GET MOVIE BY ID
    @GetMapping("/{movieId}")
    public ResponseEntity<Map<String, Object>> getMovieById(@PathVariable String movieId) {
        try {
            MovieMongoDB movie = movieService.getMovieById(movieId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "MOVIE RETRIEVED SUCCESSFULLY.");
            response.put("movie", movie);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);

        } catch (MovieNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "MOVIE NOT FOUND", "details", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO GET MOVIE", "details", e.getMessage()));
        }
    }

    //SEARCH MOVIE BY TITLE OR KEYWORD
    @GetMapping("/search/titleOrKeyword")
    public ResponseEntity<ResponseWrapper<Page<SearchTitleDTO>>> getMovieByTitle(
            @RequestParam("type") TitleTypeEnum type,
            @RequestParam String label,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SearchTitleDTO> movie = movieService.getMovieByTitleOrKeyword(type, label, page, size);
            if (movie.isEmpty()) {
                return ResponseEntity.ok(new ResponseWrapper<>("TITLES FETCHED SUCCESSFULLY, BUT NO TITLES FOUND.", movie));
            }
            return ResponseEntity.ok(new ResponseWrapper<>("TITLES FETCHED SUCCESSFULLY", movie));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("ERROR SEARCHING TITLE: " + e.getMessage(), null));
        }
    }

    //SEARCH MOVIE WITH FILTERS
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
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SearchTitleDTO> movie = movieService.getMovieWithFilters(
                    type, label, genre, release_year, platform, production_countries,
                    age_certification, imdb_scores, imdb_votes, page, size
            );

            if (movie.isEmpty()) {
                return ResponseEntity.ok(new ResponseWrapper<>("TITLES FETCHED SUCCESSFULLY, BUT NO TITLES FOUND.", movie));
            }

            return ResponseEntity.ok(new ResponseWrapper<>("TITLES FETCHED SUCCESSFULLY.", movie));

        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("ERROR SEARCHING TITLES: " + e.getMessage(), null));
        }
    }

    //SEARCH NEW MOVIE BY NAME
    @GetMapping("/searchNew/movieIdByName")
    public ResponseEntity<ResponseWrapper<List<SearchNewTitleDTO>>> searchNewTitleByName(
            @RequestParam("type") TitleTypeEnum type,
            @RequestParam("title") String title,
            @RequestParam("year") Optional<Integer> year) {
        try {
            Map<Integer, List<SearchNewTitleDTO>> movies = movieService.searchNewTitleByName(type, title, year);
            Integer result = movies.keySet().stream().findFirst().orElse(null);
            return switch (result) {
                case 0 -> ResponseEntity.ok(new ResponseWrapper<>("TITLES FETCHED SUCCESSFULLY.", movies.get(0)));
                case 1 ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(type + " WITH TITLE " + title + " NOT FOUND.", movies.get(1)));
                default ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>("AN ERROR OCCURED WITH THE API.", null));
            };
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("ERROR SEARCHING TITLES: " + e.getMessage(), null));
        }
    }

    //ADD MOVIE BY ID
    @PostMapping("/addMovieById")
    public ResponseEntity<ResponseWrapper<Integer>> addTitleById(
            @RequestParam TitleTypeEnum type,
            @RequestParam String id) {
        try {
            int result = movieService.addTitleById(type, id);
            return switch (result) {
                case 0 -> ResponseEntity.ok(new ResponseWrapper<>(type + " ADDED SUCCESSFULLY", result));
                case 1 ->
                        ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(type + " WITH ID: " + id + " ALREADY EXISTS IN THE DATABASE", null));
                case 2 ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(type + " WITH ID " + id + " NOT FOUND", null));
                default ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>("ERROR ADDING TITLE: " + id, null));
            };
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("ERROR SEARCHING TITLES: " + e.getMessage(), null));
        }
    }

    //UPDATE MOVIE
    @PatchMapping("/{movieId}")
    public ResponseEntity<Object> patchMovie(
            @PathVariable String movieId,
            @RequestBody Map<String, Object> updates) {

        try {
            boolean isUpdated = movieService.patchMovie(movieId, updates);

            if (isUpdated) {
                return ResponseEntity.ok(Map.of("message", "MOVIE UPDATED SUCCESSFULLY."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "MOVIE WITH ID " + movieId + " NOT FOUND IN ONE OR BOTH DATABASES."));
            }

        } catch (InvalidUpdateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));

        } catch (MovieNotFoundInMongoException | MovieNotFoundInNeo4JException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "FAILED TO PATCH MOVIE.",
                            "details", e.getMessage()
                    ));
        }
    }

    //DELETE MOVIE BY ID
    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String movieId) {
        try {
            movieService.deleteMovie(movieId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "MOVIE WITH ID " + movieId + " HAS BEEN DELETED."));

        } catch (MovieNotFoundInMongoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (MovieNotFoundInNeo4JException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO DELETE MOVIE.", "details", e.getMessage()));
        }
    }
}
