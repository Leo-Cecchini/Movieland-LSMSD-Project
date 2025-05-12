package it.unipi.movieland.service.Movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.unipi.movieland.dto.*;
import it.unipi.movieland.exception.*;

import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.Enum.PlatformEnum;
import it.unipi.movieland.model.Enum.TitleTypeEnum;
import it.unipi.movieland.model.Movie.*;

import it.unipi.movieland.repository.Movie.*;
import it.unipi.movieland.service.exception.BusinessException;
import it.unipi.movieland.utils.apiMDB.APIRequest;
import it.unipi.movieland.utils.deserializers.SearchNewTitleDTODeserializer;
import it.unipi.movieland.utils.deserializers.TitleDeserializer;

import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieMongoDBImplementation movieMongoDBRepository;

    @Autowired
    private MovieMongoDBRepository movieMongoDBInterface;

    @Autowired
    private MovieNeo4JRepository movieNeo4jRepository;

    //METHOD TO GET ALL MOVIES
    public Page<MovieMongoDTO> getAllMovies(Pageable pageable) {

        Page<MovieMongoDB> list = movieMongoDBInterface.findAll(pageable);
        List<MovieMongoDTO> dtoList = list.getContent().stream()
                .map(MovieMongoDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, list.getTotalElements());
    }

    // METHOD TO GET MOVIE BY ID
    public MovieMongoDB getMovieById(String movieId) {
        return movieMongoDBInterface.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundInMongoException("MOVIE WITH ID " + movieId + " NOT FOUND"));
    }

    //METHOD TO SEARCH MOVIES BY TITLE OR KEYWORD
    public Page<SearchTitleDTO> getMovieByTitleOrKeyword(TitleTypeEnum type, String label, int page, int size) throws BusinessException {
        return movieMongoDBRepository.getTitleByTitleOrKeyword(type, label, PageRequest.of(page, size));
    }

    //METHOD TO SEARCH MOVIES WITH FILTERS
    public Page<SearchTitleDTO> getMovieWithFilters(TitleTypeEnum type,
                                                    Optional<String> label,
                                                    Optional<List<GenreEnum>> genre,
                                                    Optional<Integer> releaseYear,
                                                    Optional<PlatformEnum> platform,
                                                    Optional<CountryEnum> productionCountries,
                                                    Optional<String> ageCertification,
                                                    Optional<Integer> imdbScores,
                                                    Optional<Integer> imdbVotes,
                                                    int page,
                                                    int size) throws BusinessException {

        return movieMongoDBRepository.getTitleWithFilters(
                type, label, genre, releaseYear, platform, productionCountries,
                ageCertification, imdbScores, imdbVotes,
                PageRequest.of(page, size)
        );
    }

    //METHOD TO ADD MOVIE BY ID
    public int addTitleById(TitleTypeEnum typeEnum, String id) throws IOException, InterruptedException {

        Optional<MovieMongoDB> title = movieMongoDBInterface.findById(id);
        if (title.isPresent()) {
            return 1;
        }

        APIRequest api = new APIRequest();
        String response = api.getMoviebyId(typeEnum.name(), id);
        if (response == null || response.isEmpty()) {
            return 2;
        }

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray searchArray = jsonResponse.getAsJsonArray("search");

        if (searchArray != null && searchArray.isEmpty()) {
            return 2;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MovieMongoDB.class, new TitleDeserializer())
                .create();
        MovieMongoDB movie = gson.fromJson(response, MovieMongoDB.class);
        movie.setType(movie.getType());

        MovieNeo4J movieNeo4j = new MovieNeo4J(movie.getId(), movie.getTitle(), movie.getGenres());

        movieMongoDBInterface.insert(movie);
        movieNeo4jRepository.save(movieNeo4j);

        return 0;
    }

    //METHOD TO UPDATE MOVIE
    public boolean patchMovie(String movieId, Map<String, Object> updates) {

        if (updates.containsKey("_id") || updates.containsKey("id")) {
            throw new  InvalidDateFormatException("ID CANNOT BE MODIFIED.");
        }

        Optional<MovieMongoDB> movieMongo = movieMongoDBInterface.findById(movieId);
        if (movieMongo.isEmpty()) return false;

        Optional<MovieNeo4J> movieNeo4j = movieNeo4jRepository.findByImdbId(movieId);
        if (movieNeo4j.isEmpty()) return false;

        MovieMongoDB mongo = movieMongo.get();
        MovieNeo4J neo4j = movieNeo4j.get();

        ObjectMapper mapper = new ObjectMapper();

        updates.forEach((key, value) -> {
            switch (key) {
                case "title" -> {
                    mongo.setTitle((String) value);
                    neo4j.setTitle((String) value);
                }
                case "type" -> mongo.setType(TitleTypeEnum.valueOf(((String) value).toUpperCase()));
                case "description" -> mongo.setDescription((String) value);
                case "release_year" -> mongo.setReleaseYear((Integer) value);
                case "genres" -> {
                    List<String> genres = (List<String>) value;
                    List<GenreEnum> genreEnums = genres.stream()
                            .map(g -> GenreEnum.valueOf(g.toUpperCase()))
                            .toList();
                    mongo.setGenres(genreEnums);
                    neo4j.setGenres(genreEnums);
                }
                case "keywords" -> mongo.setKeywords((List<String>) value);
                case "production_countries" -> {
                    List<String> countries = (List<String>) value;
                    List<CountryEnum> countryEnums = countries.stream()
                            .map(c -> CountryEnum.valueOf(c.toUpperCase()))
                            .toList();
                    mongo.setProductionCountries(countryEnums);
                }
                case "runtime" -> mongo.setRuntime((Integer) value);
                case "poster_path" -> mongo.setPosterPath((String) value);
                case "platform" -> mongo.setPlatform((List<String>) value);
                case "revenue" -> mongo.setRevenue(Double.valueOf(value.toString()));
                case "budget" -> mongo.setBudget(Double.valueOf(value.toString()));
                case "age_certification" -> mongo.setAgeCertification((String) value);
                case "seasons" -> mongo.setSeasons((Integer) value);
            }
        });

        movieMongoDBInterface.save(mongo);
        movieNeo4jRepository.save(neo4j);
        return true;
    }

    //METHOD TO DELETE MOVIE
    @Transactional
    public void deleteMovie(String movieId) {

        Optional<MovieMongoDB> mongoMovie = movieMongoDBInterface.findById(movieId);
        if (mongoMovie.isEmpty()) {
            throw new MovieNotFoundInMongoException("MOVIE WITH ID " + movieId + " NOT FOUND.");
        }

        Optional<MovieNeo4J> neo4jMovie = movieNeo4jRepository.findById(movieId);
        if (neo4jMovie.isEmpty()) {
            throw new MovieNotFoundInNeo4JException("MOVIE WITH ID " + movieId + " NOT FOUND.");
        }

        movieMongoDBInterface.deleteById(movieId);
        movieNeo4jRepository.deleteById(movieId);
    }

    //METHOD TO SEARCH FOR MOVIES BASED ON TITLE AND OPTIONAL YEAR
    public Map<Integer, List<SearchNewTitleDTO>> searchNewTitleByName(TitleTypeEnum type,
                                                                      String title,
                                                                      Optional<Integer> year) throws IOException, InterruptedException {
        Map<Integer, List<SearchNewTitleDTO>> map = new HashMap<>();

        APIRequest api = new APIRequest();
        String response = api.getIdByTitle(type.name(), title, year);

        if(response == null){
            map.put(2, null);
            return map;
        }

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray searchArray = jsonResponse.getAsJsonArray("search");

        if(searchArray.isEmpty()){
            map.put(1, null);
            return map;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SearchNewTitleDTO.class, new SearchNewTitleDTODeserializer())
                .create();

        Type listType = new TypeToken<List<SearchNewTitleDTO>>() {}.getType();
        List<SearchNewTitleDTO> movies = gson.fromJson(searchArray, listType); //converting the titles to searchNewTitleDTO

        map.put(0, movies);
        return map;
    }

    //METHOD TO GET MOST FREQUENT ACTORS IN SPECIFIC GENRES
    public List<ActorDTO> mostFrequentActorsSpecificGenres(List<String> genres) throws BusinessException {
        return movieMongoDBInterface.mostFrequentActorsSpecificGenres(genres);
    }

    //METHOD TO GET MOST VOTED MOVIES BASED ON SPECIFIED CRITERIA
    public List<StringCountDTO> mostVotedMoviesBy(String method) throws BusinessException {
        return switch (method) {
            case "genres" -> movieMongoDBInterface.mostVotedMoviesByGenres();
            case "production_countries" -> movieMongoDBInterface.mostVotedMoviesByProductionCountries();
            default -> movieMongoDBInterface.mostVotedMoviesByKeywords();
        };
    }

    //METHOD TO GET MOST POPULAR ACTORS
    public List<ActorDTO> mostPopularActors() throws BusinessException {
        return movieMongoDBInterface.mostPopularActors();
    }

    //METHOD TO GET HIGHEST AVERAGE ACTORS IN TOP 2000 MOVIES
    public List<ActorDTO> highesAverageActorsTop2000Movies() throws BusinessException {
        return movieMongoDBInterface.highesAverageActorsTop2000Movies();
    }

    //METHOD TO GET TOTAL MOVIES BY PLATFORM
    public List<StringCountDTO> totalMoviesByPlatform() throws BusinessException {
        return movieMongoDBInterface.totalMoviesByPlatform();
    }

    //METHOD TO GET HIGHEST PROFIT DIRECTORS
    public List<StringCountDTO> highestProfitDirectors() throws BusinessException {
        return movieMongoDBInterface.highestProfitDirectors();
    }

    //METHOD TO GET BEST PLATFORM FOR TOP 1000 MOVIES
    public List<StringCountDTO> bestPlatformForTop1000Movies() throws BusinessException {
        return movieMongoDBInterface.bestPlatformForTop1000Movies();
    }

    //METHOD TO GET PERCENTAGE OF COMBINED GENRES
    public List<CombinedPercentageDTO> percentageOfCombinedGenres(List<String> genres) throws BusinessException {
        return movieMongoDBInterface.percentageOfCombinedGenres(genres);
    }

    //METHOD TO GET PERCENTAGE OF COMBINED KEYWORDS
    public List<CombinedPercentageDTO> percentageOfCombinedKeywords(List<String> keywords) throws BusinessException {
        return movieMongoDBInterface.percentageOfCombinedKeywords(keywords);
    }

    //METHOD TO FIND DIFFERENCES BETWEEN TWO LISTS
    public static List<String> findDifference(List<String> a, List<String> b) {
        Set<String> firstSet = new HashSet<>(a);
        List<String> result = new ArrayList<>();
        for (String element : b) {
            if (!firstSet.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    //METHOD TO FIND INCONSISTENCIES BETWEEN NEO4J AND MONGODB
    public List<String> inconsistenciesNeo() {
        List<String> mongoDb=movieMongoDBInterface.findAllIds().getAllIds();
        List<String> neo4j=movieNeo4jRepository.findAllIds();

        return findDifference(neo4j, mongoDb);
    }

    public List<String> inconsistenciesMongo() {
        List<String> mongoDb=movieMongoDBInterface.findAllIds().getAllIds();
        List<String> neo4j=movieNeo4jRepository.findAllIds();

        return findDifference(mongoDb, neo4j);
    }
}