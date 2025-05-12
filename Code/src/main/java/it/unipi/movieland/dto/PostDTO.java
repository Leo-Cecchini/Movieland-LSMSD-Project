package it.unipi.movieland.dto;

import java.time.LocalDateTime;

public class PostDTO {

    private String id;
    private LocalDateTime datetime;
    private String text;
    private String author;
    private String movieId;

    public PostDTO(String id, LocalDateTime datetime, String text, String author, String movieId) {
        this.id = id;
        this.datetime = datetime;
        this.text = text;
        this.author = author;
        this.movieId = movieId;
    }

    //GETTERS AND SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
}