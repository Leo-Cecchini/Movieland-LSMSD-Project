package it.unipi.movieland.controller.Manager;

import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.Manager.Manager;
import it.unipi.movieland.service.Celebrity.CelebrityService;
import it.unipi.movieland.service.Manager.ManagerService;
import it.unipi.movieland.service.Movie.MovieService;
import it.unipi.movieland.service.Review.ReviewService;
import it.unipi.movieland.service.User.UserService;
import it.unipi.movieland.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;
    @Autowired
    private CelebrityService celebrityService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ManagerService managerService;

    //GET ALL MANAGERS WITH PAGINATION
    @GetMapping("/")
    public ResponseEntity<?> getAllManagers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<Manager> managers = managerService.getAllManagers(page, size);
            if (managers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(managers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //CREATE A NEW MANAGER
    @PostMapping("/")
    public ResponseEntity<?> addManager(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        try {
            Manager manager = managerService.addManager(username, email, password);
            return new ResponseEntity<>(manager, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //AUTHENTICATE MANAGER
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {
        try {
            return new ResponseEntity<>(managerService.authenticate(username, password), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET MANAGER BY USERNAME
    @GetMapping("/{username}")
    public ResponseEntity<?> getManagerById(@PathVariable String username) {
        try {
            Manager manager = managerService.getManagerById(username);
            return new ResponseEntity<>(manager, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE MANAGER BY USERNAME
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteManager(@PathVariable String username) {
        try {
            managerService.deleteManager(username);
            return new ResponseEntity<>("Manager deleted successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET DATA INCONSISTENCIES
    @GetMapping("/inconsistency")
    public InconsistenciesDTO getInconsistencies() {
        InconsistenciesDTO inconsistenciesDTO = new InconsistenciesDTO();
        inconsistenciesDTO.setMovieMongoDb(movieService.inconsistenciesMongo());
        inconsistenciesDTO.setMovieNeo4j(movieService.inconsistenciesNeo());
        inconsistenciesDTO.setCelebrityMongoDb(celebrityService.celebrityInconsistenciesMongo());
        inconsistenciesDTO.setCelebrityNeo4j(celebrityService.celebrityInconsistenciesNeo());
        inconsistenciesDTO.setUserMongoDb(userService.inconsistenciesMongo());
        inconsistenciesDTO.setUserNeo4j(userService.inconsistenciesNeo());
        inconsistenciesDTO.setReviewMongoDb(reviewService.inconsistenciesMongo());
        inconsistenciesDTO.setReviewNeo4j(reviewService.inconsistenciesNeo());
        return inconsistenciesDTO;
    }

    //ANALYTICS: MOST FREQUENT ACTORS IN SPECIFIC GENRES
    @GetMapping("/analytics/most-frequent-actors-specific-genres")
    public ResponseEntity<ResponseWrapper<List<ActorDTO>>> mostFrequentActorsSpecificGenres(
            @RequestParam List<String> genres) {
        try {
            List<ActorDTO> actors = movieService.mostFrequentActorsSpecificGenres(genres);
            if (actors.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("No actors found for genres: " + genres, actors));
            return ResponseEntity.ok(new ResponseWrapper<>("Actors fetched successfully", actors));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching actors: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: MOST VOTED MOVIES BY PRODUCTION COUNTRIES
    @GetMapping("/analytics/most-voted-movies-by-productionCountries")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> mostVotedMoviesByProductionCountries() {
        try {
            List<StringCountDTO> movies = movieService.mostVotedMoviesBy("production_countries");
            if (movies.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", movies));
            return ResponseEntity.ok(new ResponseWrapper<>("Movies fetched successfully", movies));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching movies: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: MOST VOTED MOVIES BY KEYWORDS
    @GetMapping("/analytics/most-voted-movies-by-keywords")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> mostVotedMoviesByKeywords() {
        try {
            List<StringCountDTO> movies = movieService.mostVotedMoviesBy("keywords");
            if (movies.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", movies));
            return ResponseEntity.ok(new ResponseWrapper<>("Movies fetched successfully", movies));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching movies: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: MOST VOTED MOVIES BY GENRES
    @GetMapping("/analytics/most-voted-movies-by-genres")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> mostVotedMoviesByGenres() {
        try {
            List<StringCountDTO> movies = movieService.mostVotedMoviesBy("genres");
            if (movies.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", movies));
            return ResponseEntity.ok(new ResponseWrapper<>("Movies fetched successfully", movies));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching movies: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: MOST POPULAR ACTORS
    @GetMapping("/analytics/most-popular-actors")
    public ResponseEntity<ResponseWrapper<List<ActorDTO>>> mostPopularActors() {
        try {
            List<ActorDTO> actors = movieService.mostPopularActors();
            if (actors.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", actors));
            return ResponseEntity.ok(new ResponseWrapper<>("Actors fetched successfully", actors));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching actors: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: HIGHEST AVERAGE ACTORS IN TOP 2000 MOVIES
    @GetMapping("/analytics/highest-avg-actors-top2000-movies")
    public ResponseEntity<ResponseWrapper<List<ActorDTO>>> highesAverageActorsTop2000Movies() {
        try {
            List<ActorDTO> actors = movieService.highesAverageActorsTop2000Movies();
            if( actors.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", actors));
            return ResponseEntity.ok(new ResponseWrapper<>("Actors fetched successfully", actors));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching actors: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: TOTAL MOVIES BY PLATFORM
    @GetMapping("/analytics/total-movies-by-platform")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> totalMoviesByPlatform() {
        try {
            List<StringCountDTO> movies = movieService.totalMoviesByPlatform();
            if (movies.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", movies));
            return ResponseEntity.ok(new ResponseWrapper<>("Movies fetched successfully", movies));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error fetching movies: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: HIGHEST PROFIT DIRECTORS
    @GetMapping("/analytics/highest-profit-directors")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> highestProfitDirectors() {
        try {
            List<StringCountDTO> directors = movieService.highestProfitDirectors();
            if (directors.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", directors));
            return ResponseEntity.ok(new ResponseWrapper<>("Directors fetched successfully", directors));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error fetching directors: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: BEST PLATFORM FOR TOP 1000 MOVIES
    @GetMapping("/analytics/best-platform-for-top1000-movies")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> bestPlatformForTop1000Movies() {
        try {
            List<StringCountDTO> platforms = movieService.bestPlatformForTop1000Movies();
            if (platforms.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", platforms));
            return ResponseEntity.ok(new ResponseWrapper<>("Platforms fetched successfully", platforms));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error fetching platforms: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: PERCENTAGE OF COMBINED GENRES
    @GetMapping("/analytics/percentage-of-combined-genres")
    public ResponseEntity<ResponseWrapper<List<CombinedPercentageDTO>>> percentageOfCombinedGenres(
            @RequestParam List<String> genres) {
        try {
            List<CombinedPercentageDTO> combinedGenres = movieService.percentageOfCombinedGenres(genres);
            if (combinedGenres.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", combinedGenres));
            return ResponseEntity.ok(new ResponseWrapper<>("Genres fetched successfully", combinedGenres));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error fetching genres: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: PERCENTAGE OF COMBINED KEYWORDS
    @GetMapping("/analytics/percentage-of-combined-keywords")
    public ResponseEntity<ResponseWrapper<List<CombinedPercentageDTO>>> percentageOfCombinedKeywords(
            @RequestParam List<String> keywords) {
        try {
            List<CombinedPercentageDTO> combinedKeywords = movieService.percentageOfCombinedKeywords(keywords);
            if (combinedKeywords.isEmpty())
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", combinedKeywords));
            return ResponseEntity.ok(new ResponseWrapper<>("Keywords fetched successfully", combinedKeywords));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error fetching keywords: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    //ANALYTICS: MOVIE REVIEW RATIO
    @GetMapping("/analytics/movie-review-ratio")
    public ResponseEntity<?> movieReviewRatio() {
        try {
            List<ReviewRatioDTO> movies = reviewService.findTopMoviesByReviewRatio();
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

