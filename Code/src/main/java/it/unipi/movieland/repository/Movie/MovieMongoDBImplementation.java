package it.unipi.movieland.repository.Movie;

import it.unipi.movieland.dto.SearchTitleDTO;
import it.unipi.movieland.model.Enum.*;
import it.unipi.movieland.service.exception.BusinessException;
import org.bson.Document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieMongoDBImplementation {

    @Autowired
    private MongoTemplate mongoTemplate;

    //METHOD TO GET TITLES BY TITLE OR KEYWORD USING AGGREGATION
    public Page<SearchTitleDTO> getTitleByTitleOrKeyword(TitleTypeEnum type, String label, Pageable pageable) throws BusinessException {
        try {
            MatchOperation matchOperation = Aggregation.match(Criteria.where("$text").is(new Document("$search", label)).and("type").is(type));

            ProjectionOperation projection = Aggregation.project("_id", "title", "release_year", "poster_path", "imdb_score")
                    .andExpression("{$meta: 'textScore'}").as("score");

            SortOperation sort = Aggregation.sort(Sort.by(Sort.Order.desc("score"), Sort.Order.desc("imdb_score")));

            Aggregation aggregation = Aggregation.newAggregation(
                    matchOperation, projection, sort,
                    Aggregation.skip(pageable.getOffset()),
                    Aggregation.limit(pageable.getPageSize())
            );

            List<SearchTitleDTO> movies = mongoTemplate.aggregate(aggregation, "Movies", SearchTitleDTO.class).getMappedResults();
            return new PageImpl<>(movies, pageable, movies.size());
        } catch (Exception e) {
            throw new BusinessException("Error retrieving titles", e);
        }
    }

    //METHOD TO GET TITLES WITH MULTIPLE FILTERS USING AGGREGATION
    public Page<SearchTitleDTO> getTitleWithFilters(TitleTypeEnum type, Optional<String> label,
                                                    Optional<List<GenreEnum>> genres, Optional<Integer> releaseYear,
                                                    Optional<PlatformEnum> platform, Optional<CountryEnum> productionCountries,
                                                    Optional<String> ageCertification, Optional<Integer> imdbScores,
                                                    Optional<Integer> imdbVotes, Pageable pageable) throws BusinessException {
        try {
            List<Criteria> criteriaList = new ArrayList<>();
            criteriaList.add(Criteria.where("type").is(type));

            label.ifPresent(l -> criteriaList.add(Criteria.where("$text").is(new Document("$search", l))));

            genres.ifPresent(g -> criteriaList.add(Criteria.where("genres").all(g)));

            releaseYear.ifPresent(y -> criteriaList.add(Criteria.where("release_year").is(y)));

            platform.ifPresent(p -> criteriaList.add(Criteria.where("platform").is(p.getDisplayName())));

            productionCountries.ifPresent(pc -> criteriaList.add(Criteria.where("production_countries").is(pc)));

            ageCertification.ifPresent(ac -> criteriaList.add(Criteria.where("age_certification").is(ac)));

            imdbScores.ifPresent(score -> criteriaList.add(Criteria.where("imdb_score").gte(score)));

            imdbVotes.ifPresent(votes -> criteriaList.add(Criteria.where("imdb_votes").gte(votes)));

            Criteria combinedCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
            MatchOperation matchOperation = Aggregation.match(combinedCriteria);

            ProjectionOperation projection = Aggregation.project("_id", "title", "release_year", "poster_path", "imdb_score")
                    .andExpression("{$meta: 'textScore'}").as("score");

            SortOperation sort = Aggregation.sort(Sort.by(Sort.Order.desc("score"), Sort.Order.desc("imdb_score")));

            long total = mongoTemplate.count(new Query(combinedCriteria), "Movies");

            Aggregation aggregation = Aggregation.newAggregation(
                    matchOperation, projection, sort,
                    Aggregation.skip(pageable.getOffset()),
                    Aggregation.limit(pageable.getPageSize())
            );

            List<SearchTitleDTO> movies = mongoTemplate.aggregate(aggregation, "Movies", SearchTitleDTO.class).getMappedResults();

            return new PageImpl<>(movies, pageable, total);
        } catch (Exception e) {
            throw new BusinessException("Error retrieving titles with filters", e);
        }
    }
}