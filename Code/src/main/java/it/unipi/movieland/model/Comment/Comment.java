package it.unipi.movieland.model.Comment;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
@Document(collection = "Comments")
public class Comment {
    @Id
    private String _id;
    private String datetime;
    private String text;
    private String author;

    // Costruttore
    public Comment(String _id, String datetime, String text, String author) {
        this._id = _id;
        this.datetime = datetime;
        this.text = text;
        this.author = author;
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

    @Override
    public String toString() {
        return "Comment{" +
                "_id='" + _id + '\'' +
                ", datetime='" + datetime + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}

