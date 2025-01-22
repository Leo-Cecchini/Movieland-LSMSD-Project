package it.unipi.movieland.model.Post;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Document(collection = "Posts")
public class Post {
    @Id
    private String _id;
    private String datetime;
    private String text;
    private String author;
    private String movieId;
    private List<Response> response;

    // Costruttore
    public Post(String _id, String datetime, String text, String author, String movieId, List<Response> response) {
        this._id = _id;
        this.datetime = datetime;
        this.text = text;
        this.author = author;
        this.movieId = movieId;
        this.response = response != null ? new ArrayList<>(response) : new ArrayList<>();
    }

    // Getter e Setter
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

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

    public List<Response> getResponse() {
        return new ArrayList<>(response);
    }

    public void setResponse(List<Response> response) {
        this.response = response != null ? new ArrayList<>(response) : new ArrayList<>();
    }

    // Aggiungi una singola risposta
    public void addResponse(Response singleResponse) {
        if (singleResponse != null) {
            this.response.add(singleResponse);
        }
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
                ", response=" + response +
                '}';
    }
}
