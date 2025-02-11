package it.unipi.movieland.service.Movie;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.Movie.*;
import it.unipi.movieland.model.*;
import it.unipi.movieland.repository.Movie.*;
import it.unipi.movieland.service.exception.BusinessException;
import it.unipi.movieland.utils.apiMDB.APIRequest;
import it.unipi.movieland.utils.deserializers.SearchNewTitleDTODeserializer;
import it.unipi.movieland.utils.deserializers.TitleDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieMongoDBImplementation movieMongoDBRepo;
    @Autowired
    private MovieMongoDBRepository movieMongoDBInterface;

    @Autowired
    private MovieNeo4jRepository movieNeo4jRepository;

    // Get all movies
    public List<Movie> getAllMovies() {
        return movieMongoDBInterface.findAll();
    }

    // Search movie by id
    public Optional<Movie> getMovieById(String movieId) {
        return movieMongoDBInterface.findById(movieId);
    }

    // Search movie by name
    public Page<SearchTitleDTO> getMovieByTitleOrKeyword(TitleTypeEnum type, String label, int page, int size) throws BusinessException {
        PageRequest pageRequest = PageRequest.of(page, size);
        return movieMongoDBRepo.getTitleByTitleOrKeyword(type, label, pageRequest);
    }

    public Page<SearchTitleDTO> getTitleWithFilters(TitleTypeEnum type,
                                                    Optional<String> label,
                                                    Optional<List<String>> genre,
                                                    Optional<Integer> release_year,
                                                    Optional<PlatformEnum> platform,
                                                    Optional<String> production_countries,
                                                    Optional<String> age_certification,
                                                    Optional<Integer> imdb_scores,
                                                    Optional<Integer> imdb_votes,
                                                    int page,
                                                    int size
    ) throws BusinessException {
        PageRequest pageRequest = PageRequest.of(page, size);
        return movieMongoDBRepo.getTitlewithFilters(type, label, genre, release_year, platform, production_countries, age_certification, imdb_scores, imdb_votes, pageRequest);
    }

    /*public Optional <List<Review>> getReviewsByMovieId(String movieId){
        return movieRepository.getReviewsByMovieId(movieId);
    }*/

    public Map<Integer,List<SearchNewTitleDTO>> searchNewTitleByName(TitleTypeEnum type,
                                                                     String title,
                                                                     Optional<Integer> year) throws IOException, InterruptedException {

        Map<Integer,List<SearchNewTitleDTO>> map = new HashMap<>();
        //creating api request and getting the response
        APIRequest api = new APIRequest();
        String response = api.getIdByTitle(type.name(), title, year);

        if(response == null){   //api error: code 2
            map.put(2, null);
            return map;
        }
        //parsing the response to a json object
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        //creating an array of json object because I can have a list of more than one title
        JsonArray searchArray = jsonResponse.getAsJsonArray("search");

        if(searchArray.isEmpty()){    //no title found: code 1
            map.put(1, null);
            return map;
        }

        //gson for the deserialization of the json object
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SearchNewTitleDTO.class, new SearchNewTitleDTODeserializer())
                .create();

        Type listType = new TypeToken<List<SearchNewTitleDTO>>() {}.getType();
        List<SearchNewTitleDTO> movies = gson.fromJson(searchArray, listType); //converting the titles to searchNewTitleDTO

        map.put(0, movies);     //no errors: code 0
        return map;
    }

    public int addTitleById(TitleTypeEnum typeEnum, String id) throws IOException, InterruptedException {
        Optional<Movie> title = movieMongoDBInterface.findById(id);
        if(title.isPresent())   //title already in the database
            return 1;

        //creating api request and getting the response
        APIRequest api = new APIRequest();
        String response = api.getMoviebyId(typeEnum.name(), id);

        if(response == null){ //api error
            return 2;
        }
        System.out.println(response);
        //parsing the response to a json object
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        //creating an array of json object because I can have a list of more than one title
        JsonArray searchArray = jsonResponse.getAsJsonArray("search");

        if( searchArray != null && searchArray.isEmpty()){  //no titles found
            return 1;
        }

        //fetching fields of json object
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Movie.class, new TitleDeserializer())
                .create();
        //create movie
        Movie movie = gson.fromJson(response, Movie.class);
        movie.setType(movie.getType());

        MovieNeo4j movieNeo4j = new MovieNeo4j(movie.get_id(), movie.getTitle(), movie.getGenres());

        //add movie to mongodb
        movieMongoDBInterface.insert(movie);
        //add movie to neo4j
        movieNeo4jRepository.save(movieNeo4j);

        return 0;
    }

    // Update a movie
    public boolean updateMovie(String movieId, UpdateTitleDTO movie){

        Optional<Movie> movieMongo = movieMongoDBInterface.findById(movieId);
        if(movieMongo.isEmpty()){  //no movie with _id = movie_id in MongoDB
            return false;
        }

        Optional<MovieNeo4j> movieNeo4j = movieNeo4jRepository.findByImdbId(movieId);
        if(movieNeo4j.isEmpty()){ //no movie with _id = movie_id in Neo4j
            return false;
        }

        //convert movie into movieNeo4j (imdb_id, title, genres)
        movieNeo4j.get().setImdb_id(movieId);
        movieNeo4j.get().setTitle(movie.getTitle());
        movieNeo4j.get().setGenres((movie.getGenres()));

        movieMongo.get().set_id(movieId);
        movieMongo.get().setTitle(movie.getTitle());
        movieMongo.get().setType(movie.getType());
        movieMongo.get().setDescription(movie.getDescription());
        movieMongo.get().setRelease_year(movie.getRelease_year());
        movieMongo.get().setGenres(movie.getGenres());
        movieMongo.get().setKeywords(movie.getKeywords());
        movieMongo.get().setProduction_countries(movie.getProduction_countries());
        movieMongo.get().setRuntime(movie.getRuntime());
        movieMongo.get().setPoster_path(movie.getPoster_path());
        movieMongo.get().setPlatform(movie.getPlatform());
        movieMongo.get().setRevenue(movie.getRevenue());
        movieMongo.get().setBudget(movie.getBudget());
        movieMongo.get().setAge_certification(movie.getAge_certification());
        movieMongo.get().setSeasons(movie.getSeasons());

        //update mongoDB
        movieMongoDBInterface.save(movieMongo.get());
        //update neo4j
        movieNeo4jRepository.save(movieNeo4j.get());
        return true;
    }

    public int addRole(String movie_id, Integer actor_id, String name, String character) {
        return movieMongoDBRepo.addRole(movie_id, actor_id, name, character);
    }

    public int updateRole(String movie_id, Integer actor_id, String name, String character) {
        return movieMongoDBRepo.updateRole(movie_id, actor_id, name, character);
    }

    public int deleteRole(String movie_id, Integer actor_id) {
        return movieMongoDBRepo.deleteRole(movie_id, actor_id);
    }

    public int addDirector(String movie_id, Integer director_id, String name) {
        return movieMongoDBRepo.addDirector(movie_id, director_id, name);
    }

    public int updateDirector(String movie_id, Integer director_id, String name) {
        return movieMongoDBRepo.updateDirector(movie_id, director_id, name);
    }

    public int deleteDirector(String movie_id, Integer director_id) {
        return movieMongoDBRepo.deleteDirector(movie_id, director_id);
    }

    // delete movie by ID
    public void deleteMovie(String movieId) {
        movieMongoDBInterface.deleteById(movieId);
        movieNeo4jRepository.deleteById(movieId);
    }

    public List<ActorDTO> mostFrequentActorsSpecificGenres(List<String> genres) throws BusinessException {
            return movieMongoDBInterface.mostFrequentActorsSpecificGenres(genres);
    }

    public List<StringCountDTO> mostVotedMoviesBy(String method) throws BusinessException {
        return switch (method) {
            case "genres" -> movieMongoDBInterface.mostVotedMoviesByGenres();
            case "production_countries" -> movieMongoDBInterface.mostVotedMoviesByProductionCountries();
            default -> movieMongoDBInterface.mostVotedMoviesByKeywords();
        };
    }

    public List<ActorDTO> mostPopularActors() throws BusinessException {
            return movieMongoDBInterface.mostPopularActors();
    }

    public List<ActorDTO> highesAverageActorsTop2000Movies() throws BusinessException {
            return movieMongoDBInterface.highesAverageActorsTop2000Movies();
    }

    public List<StringCountDTO> totalMoviesByPlatform() throws BusinessException {
            return movieMongoDBInterface.totalMoviesByPlatform();
    }

    public List<StringCountDTO> highestProfitDirectors() throws BusinessException {
            return movieMongoDBInterface.highestProfitDirectors();
    }

    public List<StringCountDTO> bestPlatformForTop1000Movies() throws BusinessException {
            return movieMongoDBInterface.bestPlatformForTop1000Movies();
    }

    public List<CombinedPercentageDTO> percentageOfCombinedGenres(List<String> genres) throws BusinessException {
            return movieMongoDBInterface.percentageOfCombinedGenres(genres);
    }

    public List<CombinedPercentageDTO> percentageOfCombinedKeywords(List<String> keywords) throws BusinessException {
            return movieMongoDBInterface.percentageOfCombinedKeywords(keywords);
    }

}
