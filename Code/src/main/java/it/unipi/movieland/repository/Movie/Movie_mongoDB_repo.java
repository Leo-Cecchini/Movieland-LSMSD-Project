package it.unipi.movieland.repository.Movie;

import com.mongodb.client.result.UpdateResult;
import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.Movie.Actor;
import it.unipi.movieland.model.Movie.Movie;
import it.unipi.movieland.model.PlatformEnum;
import it.unipi.movieland.model.TitleTypeEnum;
import it.unipi.movieland.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class Movie_mongoDB_repo {

    @Autowired
    private Movie_mongoDB_interface movie_mongoDB_interface;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Movie> getAllMovies() {
        return movie_mongoDB_interface.findAll();
    }

    public Optional<Movie> getTitleById(String id) {
        return movie_mongoDB_interface.findById(id);
    }

    public Page<SearchTitleDTO> getTitleByTitleOrKeyword(TitleTypeEnum type, String label, Pageable pageable) throws BusinessException {
        try {
            //in case label has multiple words (ex. "star wars")
            String[] words = label.split("\\s+"); // Divide the string by spaces (even consecutive spaces)
            String regex = String.join(".*", words); //add .* prefix for regex
            Criteria combinedCriteria = new Criteria().andOperator(
                    new Criteria().orOperator(
                            Criteria.where("title").regex(regex, "i"),
                            Criteria.where("keywords").regex(regex, "i")
                    ),
                    Criteria.where("type").is(type) // "type" field filter
            );

            // Aggregation definition
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(combinedCriteria),
                    Aggregation.sort(Sort.Direction.DESC, "imdb_score"), // Sort by imdb_score
                    Aggregation.skip(pageable.getOffset()),
                    Aggregation.limit(pageable.getPageSize()),
                    Aggregation.project("_id", "title", "release_year", "poster_path", "imdb_score") // Field selection
            );

            // Aggregation execution
            AggregationResults<SearchTitleDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", SearchTitleDTO.class
            );
            List<SearchTitleDTO> queryResults = mongoTemplate.aggregate(aggregation, "Movies", SearchTitleDTO.class).getMappedResults();
            long total = mongoTemplate.count(Query.query(combinedCriteria), "Movies");
            return new PageImpl<>(queryResults, pageable, total);
        } catch (Exception e) {
            throw new BusinessException("Error retrieving titles", e);
        }
    }

    public Page<SearchTitleDTO> getTitlewithFilters(
            TitleTypeEnum type,
            Optional<String> label,
            Optional<List<String>> genre,
            Optional<Integer> release_year,
            Optional<PlatformEnum> platform,
            Optional<String> production_countries,
            Optional<String> age_certification,
            Optional<Integer> imdb_scores,
            Optional<Integer> imdb_votes,
            Pageable pageable
    ) throws BusinessException {

        try {
            Criteria criteria = Criteria.where("type").is(type);
            List<Criteria> additionalCriteria = new ArrayList<>();

            //adding criteria based on optional fields
            genre.ifPresent(strings -> additionalCriteria.add(Criteria.where("genres").all(strings)));
            release_year.ifPresent(integer -> additionalCriteria.add(Criteria.where("release_year").is(integer)));
            platform.ifPresent(platformEnum -> additionalCriteria.add(Criteria.where("platform").is(platformEnum.getDisplayName())));
            production_countries.ifPresent(s -> additionalCriteria.add(Criteria.where("production_countries").is(s)));
            age_certification.ifPresent(s -> additionalCriteria.add(Criteria.where("age_certification").is(s)));
            imdb_scores.ifPresent(integer -> additionalCriteria.add(Criteria.where("imdb_score").gte(integer)));
            imdb_votes.ifPresent(integer -> additionalCriteria.add(Criteria.where("imdb_votes").gte(integer)));

            //label filter if present
            if (label.isPresent() && !label.get().isBlank()) {
                String[] words = label.get().trim().split("\\s+"); // Divide per spazi, gestisce anche più spazi consecutivi
                String regex = String.join(".*", words);
                additionalCriteria.add(new Criteria().orOperator(
                        Criteria.where("title").regex(regex, "i"),
                        Criteria.where("keywords").regex(regex, "i")
                ));
            }

            // combine alla criteria with andOperator
            if (!additionalCriteria.isEmpty()) {
                criteria = new Criteria().andOperator(Stream.concat(Stream.of(criteria), additionalCriteria.stream())
                        .toArray(Criteria[]::new));
            }

            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(criteria),
                    Aggregation.sort(Sort.Direction.DESC, "imdb_score"), // Ordina per punteggio IMDb
                    Aggregation.skip(pageable.getOffset()),
                    Aggregation.limit(pageable.getPageSize()),
                    Aggregation.project("_id", "title", "release_year", "poster_path", "imdb_score") // Seleziona i campi
            );

            AggregationResults<SearchTitleDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", SearchTitleDTO.class
            );
            List<SearchTitleDTO> queryResults = mongoTemplate.aggregate(aggregation, "Movies", SearchTitleDTO.class).getMappedResults();
            long total = mongoTemplate.count(Query.query(criteria), "Movies");
            return new PageImpl<>(queryResults, pageable, total);
        } catch (Exception e) {
            throw new BusinessException("Error retrieving titles", e);
        }

    }


    /*public Optional <List<Review>> getReviewsByMovieId(String movieId){
        // query....
        return movie_mongoDB_interface.getReviewsByMovieId(movieId);
    }*/


    public Movie addTitle(Movie movie) {
        return movie_mongoDB_interface.insert(movie);
    }

    public Movie updateTitle(Movie movie) {
        return movie_mongoDB_interface.save(movie);
    }

    public void deleteTitle(String id) {
        movie_mongoDB_interface.deleteById(id);
    }

    public int addRole(String movie_id, Integer actor_id, String name, String character) {
        // find the movie
        Query query = new Query(Criteria.where("_id").is(movie_id));

        // Costruisci l'operazione di update
        Update update = new Update().push("actors", new Actor(actor_id, name, character));

        UpdateResult result = mongoTemplate.updateFirst(query, update, Movie.class);

        if (result.getMatchedCount() > 0) {
            // movie found
            if (result.getModifiedCount() > 0) {
                System.out.println("Actor added successfully!");
                return 0;
            } else {
                // no update made
                System.out.println("Actor already exists or no update was made.");
                return 1;
            }
        } else {
            System.out.println("Movie not found!");
            return 2;
        }
    }

    public int updateRole(String movie_id, Integer actor_id, String name, String character) {
        return 0;
    }

    public int deleteRole(String movie_id, Integer actor_id) {
        return 0;
    }

    public int addDirector(String movie_id, Integer director_id, String name) {
        return 0;
    }

    public int updateDirector(String movie_id, Integer director_id, String name) {
        return 0;
    }

    public int deleteDirector(String movie_id, Integer director_id) {
        return 0;
    }

    public List<ActorDTO> mostFrequentActorsSpecificGenres(List<String> genres) throws BusinessException {

        try {
            MatchOperation matchGenres = Aggregation.match(
                    Criteria.where("genres").all(genres)
            );

            UnwindOperation unwindActors = Aggregation.unwind("actors");

            GroupOperation groupByActor = Aggregation.group("actors.id")
                    .first("actors.name").as("name")
                    .first("actors.poster").as("poster")
                    .count().as("movieCount");

            SortOperation sortByCount = Aggregation.sort(Sort.Direction.DESC, "movieCount");

            LimitOperation limitResults = Aggregation.limit(5);

            ProjectionOperation projectFields = Aggregation.project()
                    .andExpression("_id").as("actor_id")
                    .andInclude("name")
                    .andInclude("poster")
                    .andInclude("movieCount");

            // aggregation pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    matchGenres,
                    unwindActors,
                    groupByActor,
                    sortByCount,
                    limitResults,
                    projectFields
            );

            AggregationResults<ActorDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", ActorDTO.class
            );
            return results.getMappedResults();
        } catch (Exception e) {
            throw new BusinessException("Error retrieving actors", e);
        }
    }

    public List<StringCountDTO> mostVotedMoviesBy(String method) throws BusinessException {
        try {

            // sorting by imdb_votes
            SortOperation sortByVotes = Aggregation.sort(Sort.Direction.DESC, "imdb_votes");

            // limit to documents fetched
            LimitOperation limitResults = Aggregation.limit(1000);

            // exploiting production countries array
            UnwindOperation unwindCountries = Aggregation.unwind(method);

            // group by production_countries and imdb_votes count
            GroupOperation groupByCountry = Aggregation.group(method)
                    .sum("imdb_votes").as("count");

            // sorting by count
            SortOperation sortByCount = Aggregation.sort(Sort.Direction.DESC, "count");

            ProjectionOperation projectFields = Aggregation.project()
                    .andExpression("_id").as("label")
                    .andExpression("count").as("count");


            //  pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    sortByVotes,
                    limitResults,
                    unwindCountries,
                    groupByCountry,
                    sortByCount,
                    projectFields
            );

            AggregationResults<StringCountDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", StringCountDTO.class
            );

            return results.getMappedResults();

        } catch (Exception e) {
            throw new BusinessException("Error retrieving movies", e);
        }
    }

    public List<ActorDTO> mostPopularActors() throws BusinessException {
        try {
            // Sorting by imdb_votes
            SortOperation sortByVotes = Aggregation.sort(Sort.Direction.DESC, "imdb_votes");

            // Limiting 1000 movies fetched
            LimitOperation limitMovies = Aggregation.limit(100);

            // actors lookup
            LookupOperation lookupActors = Aggregation.lookup("Celebrities", "_id", "jobs.movie_id", "actors");

            // actors array unwind
            UnwindOperation unwindActors = Aggregation.unwind("actors");

            // group by actor
            GroupOperation groupByActor = Aggregation.group("actors._id")
                    .first("actors.name").as("name")
                    .first("actors.Poster").as("poster")
                    .count().as("movieCount");

            // sorting by movieCount
            SortOperation sortByCount = Aggregation.sort(Sort.Direction.DESC, "movieCount");

            // limit to 10 actors
            LimitOperation limitActors = Aggregation.limit(10);

            ProjectionOperation projectFields = Aggregation.project()
                    .andExpression("_id").as("actor_id")
                    .andInclude("name")
                    .andInclude("poster")
                    .andInclude("movieCount");

            Aggregation aggregation = Aggregation.newAggregation(
                    sortByVotes,
                    limitMovies,
                    lookupActors,
                    unwindActors,
                    groupByActor,
                    sortByCount,
                    limitActors,
                    projectFields
            );
            return mongoTemplate.aggregate(aggregation, "Movies", ActorDTO.class).getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public List<ActorDTO> highesAverageActorsTop2000Movies() throws BusinessException {
        try {
            // sorting by imdb_votes
            SortOperation sortByVotes = Aggregation.sort(Sort.Direction.DESC, "imdb_votes");

            // limit documents
            LimitOperation limitMovies = Aggregation.limit(1000);

            // actors lookup
            LookupOperation lookupActors = Aggregation.lookup("Celebrities", "_id", "jobs.movie_id", "actors");

            // actors array unwind
            UnwindOperation unwindActors = Aggregation.unwind("actors");

            // group by actors with average score and movie counting
            GroupOperation groupByActor = Aggregation.group("actors._id")
                    .first("actors.name").as("name")
                    .first("actors.Poster").as("poster")
                    .avg("imdb_score").as("avg_score")
                    .count().as("movieCount");

            // filter for only actors with at least 5 movies
            MatchOperation matchFilmsCount = Aggregation.match(Criteria.where("movieCount").gte(5));

            // sorting by average score
            SortOperation sortByAvgScore = Aggregation.sort(Sort.Direction.DESC, "avg_score");

            // limit showed actors
            LimitOperation limitActors = Aggregation.limit(10);

            // fields projection
            ProjectionOperation projectFields = Aggregation.project()
                    .andExpression("_id").as("actor_id")
                    .andExpression("name").as("name")
                    .andExpression("poster").as("poster")
                    .andExpression("avg_score").as("averageScore")
                    .andExpression("movieCount").as("movieCount");

            // pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    sortByVotes,
                    limitMovies,
                    lookupActors,
                    unwindActors,
                    groupByActor,
                    matchFilmsCount,
                    sortByAvgScore,
                    limitActors,
                    projectFields
            );

            return mongoTemplate.aggregate(aggregation, "Movies", ActorDTO.class).getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public List<StringCountDTO> totalMoviesByPlatform() throws BusinessException {
        try {
            // exploiting production countries array
            UnwindOperation unwindPlatform = Aggregation.unwind("platform");

            // group by production_countries and imdb_votes count
            GroupOperation groupByCountry = Aggregation.group("platform")
                    .count().as("count");

            // sorting by count
            SortOperation sortByCount = Aggregation.sort(Sort.Direction.DESC, "count");

            ProjectionOperation projectFields = Aggregation.project()
                    .andExpression("_id").as("label")
                    .andExpression("count").as("count");

            //  pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    unwindPlatform,
                    groupByCountry,
                    sortByCount,
                    projectFields
            );

            AggregationResults<StringCountDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", StringCountDTO.class
            );
            return results.getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public List<StringCountDTO> highestProfitDirectors() throws BusinessException {
        try {
            // Add a computed field "profit" as revenue - budget
            AddFieldsOperation addProfitField = Aggregation.addFields()
                    .addField("profit").withValue(ArithmeticOperators.Subtract.valueOf("revenue").subtract("budget"))
                    .build();

            // Filter documents where "profit" is not null
            MatchOperation matchProfitNotNull = Aggregation.match(Criteria.where("profit").ne(null));

            // Unwind the "director" field
            UnwindOperation unwindDirector = Aggregation.unwind("director");

            // Group by director and calculate average profit
            GroupOperation groupByDirector = Aggregation.group("director")
                    .first("director.name").as("director")
                    .avg("profit").as("average");

            // Sort by average profit in descending order
            SortOperation sortByAverage = Aggregation.sort(Sort.Direction.DESC, "average");

            // Limit to top 100 directors
            LimitOperation limitTop100 = Aggregation.limit(100);

            ProjectionOperation projectFields = Aggregation.project()
                    .andExpression("director").as("label")
                    .andExpression("average").as("count");

            // Aggregation pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    addProfitField,
                    matchProfitNotNull,
                    unwindDirector,
                    groupByDirector,
                    sortByAverage,
                    limitTop100,
                    projectFields
            );

            return mongoTemplate.aggregate(aggregation, "Movies", StringCountDTO.class)
                    .getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public List<StringCountDTO> bestPlatformForTop1000Movies() throws BusinessException {
        try{
            // sorting by imdb_votes in descending order
            SortOperation sortByVotes = Aggregation.sort(Sort.Direction.DESC, "imdb_votes");

            // limiting the result to 1000 documents
            LimitOperation limitResults = Aggregation.limit(1000);

            // unwinding the platform field
            UnwindOperation unwindPlatform = Aggregation.unwind("platform");

            // grouping by platform and counting occurrences
            GroupOperation groupByPlatform = Aggregation.group("platform")
                    .count().as("count");

            // Sorting by count in descending order
            SortOperation sortByCount = Aggregation.sort(Sort.Direction.DESC, "count");

            ProjectionOperation projectFields = Aggregation.project()
                    .andExpression("_id").as("label")
                    .andExpression("count").as("count");

            // pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    sortByVotes,
                    limitResults,
                    unwindPlatform,
                    groupByPlatform,
                    sortByCount,
                    projectFields
            );

            AggregationResults<StringCountDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", StringCountDTO.class
            );

            return results.getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public List<CombinedPercentageDTO> percentageOfCombinedGenres(List<String> genres) throws BusinessException {
        try{
            //match movies for genres given by the user
            MatchOperation matchGenres = Aggregation.match(
                    Criteria.where("genres").all(genres)
            );

            // count total number of movies and cumulate genres
            GroupOperation groupForTotalAndGenres = Aggregation.group()
                    .count().as("totalMovies")
                    .push("genres").as("genreCounts");

            // unwind for genres array
            UnwindOperation unwindGenreCounts = Aggregation.unwind("genreCounts");

            // unwind for single genre
            UnwindOperation unwindSingleGenre = Aggregation.unwind("genreCounts");

            // count occurencies of each genre
            GroupOperation groupByGenre = Aggregation.group("genreCounts")
                    .count().as("movieCount")
                    .first("totalMovies").as("totalMovies");

            // projection for selecting fields and compute the combined percentage
            ProjectionOperation projectFields = Aggregation.project()
                    .and("_id").as("label")
                    .andInclude("movieCount")
                    .andExpression("($movieCount / $totalMovies) * 100").as("combinedPercentage");

            // sorting by percentage
            SortOperation sortByPercentage = Aggregation.sort(Sort.Direction.DESC, "combinedPercentage");

            // Pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    matchGenres,
                    groupForTotalAndGenres,
                    unwindGenreCounts,
                    unwindSingleGenre,
                    groupByGenre,
                    projectFields,
                    sortByPercentage
            );
            AggregationResults<CombinedPercentageDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", CombinedPercentageDTO.class
            );
            return results.getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public List<CombinedPercentageDTO> percentageOfCombinedKeywords(List<String> keywords) throws BusinessException {
        try{
            //match movies for keywords given by the user
            MatchOperation matchKeywords = Aggregation.match(
                    Criteria.where("keywords").all(keywords)
            );

            // count total number of movies and cumulate genres
            GroupOperation groupForTotalAndKeywords = Aggregation.group()
                    .count().as("totalMovies")
                    .push("keywords").as("keywordCounts");

            // unwind for genres array
            UnwindOperation unwindKeywordsCounts = Aggregation.unwind("keywordCounts");

            // unwind for single genre
            UnwindOperation unwindSingleKeyword = Aggregation.unwind("keywordCounts");

            // count occurencies of each genre
            GroupOperation groupByKeyword = Aggregation.group("keywordCounts")
                    .count().as("movieCount")
                    .first("totalMovies").as("totalMovies");

            // projection for selecting fields and compute the combined percentage
            ProjectionOperation projectFields = Aggregation.project()
                    .and("_id").as("label")
                    .andInclude("movieCount")
                    .andExpression("($movieCount / $totalMovies) * 100").as("combinedPercentage");

            // sorting by percentage
            SortOperation sortByPercentage = Aggregation.sort(Sort.Direction.DESC, "combinedPercentage");

            // Pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    matchKeywords,
                    groupForTotalAndKeywords,
                    unwindKeywordsCounts,
                    unwindSingleKeyword,
                    groupByKeyword,
                    projectFields,
                    sortByPercentage
            );
            AggregationResults<CombinedPercentageDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", CombinedPercentageDTO.class
            );
            return results.getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

}

