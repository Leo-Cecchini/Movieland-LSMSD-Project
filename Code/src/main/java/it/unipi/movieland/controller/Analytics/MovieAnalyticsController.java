package it.unipi.movieland.controller.Analytics;

import it.unipi.movieland.dto.*;
import it.unipi.movieland.service.Movie.MovieService;
import it.unipi.movieland.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movie-statistics")
public class MovieAnalyticsController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/most-frequent-actors-specific-genres")
    public ResponseEntity<ResponseWrapper<List<ActorDTO>>> mostFrequentActorsSpecificGenres(
            @RequestParam List<String> genres
            ){
        try{
            List<ActorDTO> actors = movieService.mostFrequentActorsSpecificGenres(genres);
            if( actors.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("No actors found for the genres: "+genres, actors));
            return ResponseEntity.ok(new ResponseWrapper<>("Actors fetched successfully", actors));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching actors: " + e.getMessage(), null));
        }
    }

    @GetMapping("/most-voted-movies-by-productionCountries")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> mostVotedMoviesByProductionCountries(){
        try {
            List<StringCountDTO> movies = movieService.mostVotedMoviesBy("production_countries");
            if( movies.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", movies));
            return ResponseEntity.ok(new ResponseWrapper<>("Movies fetched successfully", movies));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching movies: " + e.getMessage(), null));
        }
    }

    @GetMapping("/most-voted-movies-by-keywords")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> mostVotedMoviesByKeywords(){
        try {
            List<StringCountDTO> movies = movieService.mostVotedMoviesBy("keywords");
            if( movies.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", movies));
            return ResponseEntity.ok(new ResponseWrapper<>("Movies fetched successfully", movies));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching movies: " + e.getMessage(), null));
        }
    }

    @GetMapping("/most-voted-movies-by-genres")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> mostVotedMoviesByGenres(){
        try {
            List<StringCountDTO> movies = movieService.mostVotedMoviesBy("genres");
            if( movies.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", movies));
            return ResponseEntity.ok(new ResponseWrapper<>("Movies fetched successfully", movies));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching movies: " + e.getMessage(), null));
        }
    }

    @GetMapping("/most-popular-actors")
    public ResponseEntity<ResponseWrapper<List<ActorDTO>>> mostPopularActors(){
        try {
            List<ActorDTO> actors = movieService.mostPopularActors();
            if( actors.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", actors));
            return ResponseEntity.ok(new ResponseWrapper<>("Actors fetched successfully", actors));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching actors: " + e.getMessage(), null));
        }
    }

    @GetMapping("/highest-avg-actors-top2000-movies")
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

    @GetMapping("/total-movies-by-platform")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> totalMoviesByPlatform() {
        try {
            List<StringCountDTO> movies = movieService.totalMoviesByPlatform();
            if( movies.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", movies));
            return ResponseEntity.ok(new ResponseWrapper<>("Movies fetched successfully", movies));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching movies: " + e.getMessage(), null));
        }
    }

    @GetMapping("highest-profit-directors")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> highestProfitDirectors() {
        try {
            List<StringCountDTO> directors = movieService.highestProfitDirectors();
            if( directors.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", directors));
            return ResponseEntity.ok(new ResponseWrapper<>("Directors fetched successfully", directors));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching directors: " + e.getMessage(), null));
        }
    }

    @GetMapping("/best-platform-for-top1000-movies")
    public ResponseEntity<ResponseWrapper<List<StringCountDTO>>> bestPlatformForTop1000Movies() {
        try {
            List<StringCountDTO> platforms = movieService.bestPlatformForTop1000Movies();
            if( platforms.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", platforms));
            return ResponseEntity.ok(new ResponseWrapper<>("Platforms fetched successfully", platforms));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching platforms: " + e.getMessage(), null));
        }
    }

    @GetMapping("/percentage-of-combined-genres")
    public ResponseEntity<ResponseWrapper<List<CombinedPercentageDTO>>> percentageOfCombinedGenres(
            @RequestParam List<String> genres) {
        try {
            List<CombinedPercentageDTO> combinedGenres = movieService.percentageOfCombinedGenres(genres);
            if( combinedGenres.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", combinedGenres));
            return ResponseEntity.ok(new ResponseWrapper<>("Genres fetched successfully", combinedGenres));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching genres: " + e.getMessage(), null));
        }
    }

    @GetMapping("/percentage-of-combined-keywords")
    public ResponseEntity<ResponseWrapper<List<CombinedPercentageDTO>>> percentageOfCombinedKeywords(
            @RequestParam List<String> keywords) {
        try {
            List<CombinedPercentageDTO> combinedKeywords = movieService.percentageOfCombinedKeywords(keywords);
            if( combinedKeywords.isEmpty() )
                return ResponseEntity.ok(new ResponseWrapper<>("Query executed successfully but no result given", combinedKeywords));
            return ResponseEntity.ok(new ResponseWrapper<>("Keywords fetched successfully", combinedKeywords));
        }catch(BusinessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>("Error searching keywords: " + e.getMessage(), null));
        }
    }

}
