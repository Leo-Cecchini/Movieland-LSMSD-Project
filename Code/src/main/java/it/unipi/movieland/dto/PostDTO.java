package it.unipi.movieland.dto;

import java.time.LocalDateTime;

public class PostDTO {
    private String _id;
    private LocalDateTime datetime;
    private String text;
    private String author;
    private String movie_id;

    public PostDTO(String _id, LocalDateTime datetime, String text, String author, String movie_id) {
        this._id = _id;
        this.datetime = datetime;
        this.text = text;
        this.author = author;
        this.movie_id = movie_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
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

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }
}

