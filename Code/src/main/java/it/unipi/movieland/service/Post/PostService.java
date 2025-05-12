package it.unipi.movieland.service.Post;

import it.unipi.movieland.dto.PostActivityDTO;
import it.unipi.movieland.dto.PostDTO;
import it.unipi.movieland.dto.UserInfluencerDTO;

import it.unipi.movieland.exception.InvalidDateFormatException;
import it.unipi.movieland.exception.MovieNotFoundInMongoException;
import it.unipi.movieland.exception.PostNotFoundInMongoException;
import it.unipi.movieland.model.Post.PostMongoDB;

import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.repository.Post.PostMongoDBRepository;
import it.unipi.movieland.repository.User.UserMongoDBRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class PostService {

    private final UserMongoDBRepository userRepository;
    private final PostMongoDBRepository postMongoDBRepository;
    private final MovieMongoDBRepository movieMongoDBRepository;

    @Autowired
    public PostService(PostMongoDBRepository postMongoDBRepository, UserMongoDBRepository userRepository, MovieMongoDBRepository movieMongoDBRepository) {

        this.userRepository = userRepository;
        this.postMongoDBRepository = postMongoDBRepository;
        this.movieMongoDBRepository = movieMongoDBRepository;
    }

    //METHOD TO CREATE A POST
    public PostMongoDB createPost(String text, String author, String movieId) {
        if (!userRepository.existsById(author)) {
            throw new PostNotFoundInMongoException("USER WITH ID : " + author + " NOT FOUND.");
        }

        if(!movieMongoDBRepository.existsById(movieId)) {
            throw new PostNotFoundInMongoException("MOVIE WITH ID " + movieId + " NOT FOUND");
        }

        PostMongoDB post = new PostMongoDB(text,author,movieId,null);
        return postMongoDBRepository.save(post);
    }

    //METHOD TO UPDATE AN EXISTING POST
    public PostMongoDB updatePost(String id, String text) {

        PostMongoDB existingPost = postMongoDBRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundInMongoException("POST WITH ID " + id + " NOT FOUND"));

        existingPost.setText(text);
        existingPost.setDatetime(LocalDateTime.now());

        return postMongoDBRepository.save(existingPost);
    }

    //METHOD TO DELETE A POST BY ID
    public void deletePost(String id) {

        PostMongoDB existingPost = postMongoDBRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundInMongoException("POST WITH ID " + id + " NOT FOUND"));

        postMongoDBRepository.delete(existingPost);
    }

    //METHOD TO RETRIEVE A POST BY ID
    public PostMongoDB getPostById(String id) {
        Optional<PostMongoDB> post = postMongoDBRepository.findById(id);

        if (post.isEmpty()) {
            throw new PostNotFoundInMongoException("POST WITH ID " + id + " NOT FOUND");
        }
        return post.get();
    }

    //METHOD TO GET POST BY AUTHOR
    public Page<PostMongoDB> getPostByAuthor(String authorId, int page, int size) {

        boolean userExists = userRepository.existsById(authorId);

        if (!userExists) {
            throw new PostNotFoundInMongoException("AUTHOR WITH ID " + authorId + " NOT FOUND");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PostMongoDB> post = postMongoDBRepository.findByAuthor(authorId, pageRequest);

        if (post.isEmpty()) {
            throw new PostNotFoundInMongoException("NO POST FOUND FOR THE AUTHOR WITH ID " + authorId + ".");
        }

        return post;
    }

    //METHOD TO GET POSTS BY MOVIE ID
    public List<PostDTO> getPostsByMovieId(String movieId) {

        List<PostDTO> posts = postMongoDBRepository.findByMovieId(movieId);

        if (posts.isEmpty()) {
            throw new MovieNotFoundInMongoException("NO POSTS FOUND FOR MOVIE ID: " + movieId);
        }
        return posts;
    }

    //METHOD TO GET POSTS BY DATA RANGE
    public Page<PostDTO> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {

        if (startDate == null || endDate == null) {
            throw new InvalidDateFormatException("THE DATE FORMAT ENTERED IS INVALID. USE ‘yyyy-MM-dd’T’HH:mm:ss’.");
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateFormatException("THE START DATE CANNOT BE AFTER THE END DATE.");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return postMongoDBRepository.findByDatetimeBetween(startDate, endDate, pageRequest);
    }

    //METHOD TO GET POST ACTIVITY
    public List<PostActivityDTO> getPostActivity() {
        return postMongoDBRepository.getPostActivity();
    }

    //METHOD TO GET INFLUENCERS REPORT
    public List<UserInfluencerDTO> getInfluencersReport()  {
        return postMongoDBRepository.getInfluencersReport();
    }
}


