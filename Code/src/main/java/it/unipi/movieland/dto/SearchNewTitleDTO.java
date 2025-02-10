package it.unipi.movieland.dto;

import lombok.Data;

@Data
public class SearchNewTitleDTO {

    private String imdb_id;
    private String title;
    private Integer year;
    private String type;

    public SearchNewTitleDTO() {}

    public SearchNewTitleDTO(String imdb_id, Integer release_year, String title, String type) {
        this.imdb_id = imdb_id;
        this.year = release_year;
        this.title = title;
        this.type = type;
    }

    public String getImdb_id() {return imdb_id;}
    public void setImdb_id(String imdb_id) {this.imdb_id = imdb_id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public Integer getYear() {return year;}
    public void setYear(Integer year) {this.year = year;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
}
