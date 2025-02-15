package it.unipi.movieland.repository.Movie;

import com.mongodb.client.result.UpdateResult;
import it.unipi.movieland.dto.*;
import it.unipi.movieland.model.CountryEnum;
import it.unipi.movieland.model.GenreEnum;
import it.unipi.movieland.model.Movie.MovieCelebrity;
import it.unipi.movieland.model.Movie.Movie;
import it.unipi.movieland.model.PlatformEnum;
import it.unipi.movieland.model.TitleTypeEnum;
import it.unipi.movieland.service.exception.BusinessException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class MovieMongoDBImplementation {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<SearchTitleDTO> getTitleByTitleOrKeyword(TitleTypeEnum type, String label, Pageable pageable) throws BusinessException {
        try {
            MatchOperation matchOperation = Aggregation.match(
                    Criteria.where("$text").is(new Document("$search", label)).and("type").is(type)
            );

            ProjectionOperation addScoreField = Aggregation.project("_id", "title", "release_year", "poster_path", "imdb_score")
                    .andExpression("{$meta: 'textScore'}").as("score");

            SortOperation sortOperation = Aggregation.sort(
                    org.springframework.data.domain.Sort.by(
                            org.springframework.data.domain.Sort.Order.desc("score"),
                            org.springframework.data.domain.Sort.Order.desc("imdb_score")
                    )
            );

            ProjectionOperation finalProjection = Aggregation.project("_id", "title", "release_year", "poster_path", "imdb_score");

            Aggregation aggregation = Aggregation.newAggregation(
                    matchOperation,
                    addScoreField,
                    sortOperation,
                    finalProjection,
                    Aggregation.skip((long) pageable.getOffset()),
                    Aggregation.limit(pageable.getPageSize())
            );

            AggregationResults<SearchTitleDTO> results = mongoTemplate.aggregate(aggregation, "Movies", SearchTitleDTO.class);
            List<SearchTitleDTO> movies = results.getMappedResults();

            return new PageImpl<>(movies, pageable, movies.size());
            /*
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
            return new PageImpl<>(queryResults, pageable, total);*/
        } catch (Exception e) {
            throw new BusinessException("Error retrieving titles", e);
        }
    }

    public Page<SearchTitleDTO> getTitlewithFilters(TitleTypeEnum type,
            Optional<String> label,
            Optional<List<GenreEnum>> genres,
            Optional<Integer> release_year,
            Optional<PlatformEnum> platform,
            Optional<CountryEnum> production_countries,
            Optional<String> age_certification,
            Optional<Integer> imdb_scores,
            Optional<Integer> imdb_votes,
            Pageable pageable
    ) throws BusinessException {

        try {
            List<Criteria> criteriaList = new ArrayList<>();

            // Base criteria for type
            criteriaList.add(Criteria.where("type").is(type));
            // Handle text search for label if present
            if (label.isPresent() && !label.get().isEmpty()) {
                criteriaList.add(Criteria.where("$text").is(new Document("$search", label.get())));
            }

            // Add optional criteria only if they are present
            genres.ifPresent(g -> criteriaList.add(Criteria.where("genres").all(g)));
            release_year.ifPresent(y -> criteriaList.add(Criteria.where("release_year").is(y)));
            platform.ifPresent(p -> criteriaList.add(Criteria.where("platform").is(p.getDisplayName())));
            production_countries.ifPresent(pc -> criteriaList.add(Criteria.where("production_countries").is(pc)));
            age_certification.ifPresent(ac -> criteriaList.add(Criteria.where("age_certification").is(ac)));
            imdb_scores.ifPresent(score -> criteriaList.add(Criteria.where("imdb_score").gte(score)));
            imdb_votes.ifPresent(votes -> criteriaList.add(Criteria.where("imdb_votes").gte(votes)));

            // Create the match operation with all criteria
            Criteria combinedCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
            MatchOperation matchOperation = Aggregation.match(combinedCriteria);

            // Project fields and add text score
            ProjectionOperation addScoreField = Aggregation.project()
                    .and("_id").as("_id")
                    .and("title").as("title")
                    .and("release_year").as("release_year")
                    .and("poster_path").as("poster_path")
                    .and("imdb_score").as("imdb_score")
                    .andExpression("{$meta: 'textScore'}").as("score");

            // Sort by text score and imdb_score
            SortOperation sortOperation = Aggregation.sort(
                    Sort.by(Sort.Direction.DESC, "score", "imdb_score")
            );

            // Final projection to clean up output
            ProjectionOperation finalProjection = Aggregation.project()
                    .and("_id").as("_id")
                    .and("title").as("title")
                    .and("release_year").as("release_year")
                    .and("poster_path").as("poster_path")
                    .and("imdb_score").as("imdb_score");

            // Count total matches for pagination
            Aggregation countAggregation = Aggregation.newAggregation(matchOperation);
            AggregationResults<Document> countResults = mongoTemplate.aggregate(
                    countAggregation, "Movies", Document.class);
            long total = countResults.getMappedResults().size();

            // Build final aggregation with pagination
            Aggregation aggregation = Aggregation.newAggregation(
                    matchOperation,
                    addScoreField,
                    sortOperation,
                    finalProjection,
                    Aggregation.skip(pageable.getOffset()),
                    Aggregation.limit(pageable.getPageSize())
            );

            AggregationResults<SearchTitleDTO> results = mongoTemplate.aggregate(
                    aggregation, "Movies", SearchTitleDTO.class);
            List<SearchTitleDTO> movies = results.getMappedResults();

            return new PageImpl<>(movies, pageable, total);

            /*
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
             */
        } catch (Exception e) {
            throw new BusinessException("Error retrieving titles", e);
        }

    }


    public int addRole(String movie_id, Integer actor_id, String name, String character) {
        // find the movie
        Query query = new Query(Criteria.where("_id").is(movie_id));

        // Costruisci l'operazione di update
        Update update = new Update().push("actors", new MovieCelebrity(actor_id, name, character));

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

}

