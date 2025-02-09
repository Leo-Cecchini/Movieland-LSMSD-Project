package it.unipi.movieland.repository.Post;

import it.unipi.movieland.DTO.PostActivityDTO;
import it.unipi.movieland.DTO.UserInfluencerDTO;
import it.unipi.movieland.model.Post.Post;
import it.unipi.movieland.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class Post_mongoDB_repo {
    @Autowired
    private Post_mongoDB_interface post_mongoDB_interface;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<PostActivityDTO> getPostActivity() throws BusinessException {
        try{
            // add "hour" field extracting the hour from the datetime field
            ProjectionOperation addHourField = Aggregation.project()
                    .and(DateOperators.Hour.hourOf(
                            DateOperators.DateFromString.fromStringOf("datetime")
                    )).as("hour");

            // group by hour and posts count
            GroupOperation groupByHour = Aggregation.group("hour")
                    .count().as("postCount");

            // sort by hour
            SortOperation sortByHour = Aggregation.sort(Sort.Direction.ASC, "_id");

            ProjectionOperation renameIdToHour = Aggregation.project()
                    .and("_id").as("hour")
                    .andInclude("postCount");


            // Pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    addHourField,
                    groupByHour,
                    sortByHour,
                    renameIdToHour
            );

            AggregationResults<PostActivityDTO> results = mongoTemplate.aggregate(
                    aggregation, "Posts", PostActivityDTO.class
            );
            return results.getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public List<UserInfluencerDTO> getInfluencersReport() throws BusinessException {
        try{
            // group by author with count of posts and comments
            GroupOperation groupByAuthor = Aggregation.group("author")
                    .count().as("totalPosts")
                    .sum(ArrayOperators.Size.lengthOfArray("response")).as("totalComments");

            // add "commentsPerPost" field by computing comments and post
            AddFieldsOperation addCommentsPerPost = Aggregation.addFields()
                    .addFieldWithValue("commentsPerPost",
                            ConditionalOperators.when(
                                            ComparisonOperators.valueOf("totalPosts").equalToValue(0)
                                    ).then(0)
                                    .otherwise(
                                            ArithmeticOperators.Divide.valueOf("totalComments").divideBy("totalPosts")
                                    )
                    ).build();

            // sorting
            SortOperation sortByFields = Aggregation.sort(
                    Sort.by(Sort.Order.desc("totalComments"), Sort.Order.desc("totalPosts"), Sort.Order.desc("commentsPerPost"))
            );

            // rename _id in username
            ProjectionOperation projectFields = Aggregation.project()
                    .andExclude("_id")
                    .and("_id").as("username")
                    .andInclude("totalPosts", "totalComments", "commentsPerPost");

            // Pipeline
            Aggregation aggregation = Aggregation.newAggregation(
                    groupByAuthor,
                    addCommentsPerPost,
                    sortByFields,
                    projectFields
            );

            AggregationResults<UserInfluencerDTO> results = mongoTemplate.aggregate(
                    aggregation, "Posts", UserInfluencerDTO.class
            );

            return results.getMappedResults();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public Page<Post> findByDatetimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable){
        return post_mongoDB_interface.findByDatetimeBetween(startDate, endDate, pageable);
    }

}
