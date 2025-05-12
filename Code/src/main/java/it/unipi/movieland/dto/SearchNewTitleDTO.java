package it.unipi.movieland.dto;

import it.unipi.movieland.model.Enum.TitleTypeEnum;
import lombok.Data;

@Data
public class SearchNewTitleDTO {

    private String imdbId;
    private String title;
    private Integer year;
    private TitleTypeEnum type;

    public SearchNewTitleDTO() {}

    public SearchNewTitleDTO(String imdbId,String title, Integer release_year, TitleTypeEnum type) {
        this.imdbId = imdbId;
        this.year = release_year;
        this.title = title;
        this.type = type;
    }

    //GETTERS AND SETTES
    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public TitleTypeEnum getType() { return type; }
    public void setType(TitleTypeEnum type) { this.type = type; }
}
