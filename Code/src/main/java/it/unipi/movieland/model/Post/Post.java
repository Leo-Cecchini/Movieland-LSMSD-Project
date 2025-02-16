package it.unipi.movieland.model.Post;

import it.unipi.movieland.model.Comment.Comment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Posts")
public class Post {

    @Id
    private String _id;
    private LocalDateTime datetime;
    private String text;
    private String author;
    @Field("movie_id")
    private String movieId;

    public Post() {
        this.datetime = LocalDateTime.now();
    }

    // COSTRUTTORE PARAMETRIZZATO
    public Post(String text, String author, String movieId, List<Comment> comment) {
        this.datetime = LocalDateTime.now();
        this.text = text;
        this.author = author;
        this.movieId = movieId;
    }

    //GETTER E SETTER
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMovieId() {
        return movieId;
    }
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }


    // Override di toString
    @Override
    public String toString() {
        return "Post{" +
                "_id='" + _id + '\'' +
                ", datetime='" + datetime + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }
}
