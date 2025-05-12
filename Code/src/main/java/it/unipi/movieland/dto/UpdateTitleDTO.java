package it.unipi.movieland.dto;

import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.Enum.TitleTypeEnum;
import java.util.List;

public class UpdateTitleDTO {
    private String _id;
    private String title;
    private TitleTypeEnum type;
    private String description;
    private Integer release_year;
    private List<GenreEnum> genres;
    private List<String> keywords;
    private List<CountryEnum> production_countries;
    private Integer runtime;
    private String poster_path;
    private List<String> platform;
    private Double revenue;
    private Double budget;
    private String age_certification;
    private Integer seasons;

    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public TitleTypeEnum getType() {
        return type;
    }
    public void setType(TitleTypeEnum type) {
        this.type = type;
    }

    public Integer getReleaseYear() {
        return release_year;
    }
    public void setReleaseYear(Integer release_year) { this.release_year = release_year; }

    public List<GenreEnum> getGenres() {
        return genres;
    }
    public void setGenres(List<GenreEnum> genres) {
        this.genres = genres;
    }

    public List<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<CountryEnum> getProduction_countries() {
        return production_countries;
    }
    public void setProduction_countries(List<CountryEnum> production_countries) { this.production_countries = production_countries; }

    public Integer getRuntime() {
        return runtime;
    }
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getPoster_path() {
        return poster_path;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public List<String> getPlatform() {
        return platform;
    }
    public void setPlatform(List<String> platform) {
        this.platform = platform;
    }

    public Double getRevenue() {
        return revenue;
    }
    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getBudget() {
        return budget;
    }
    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getAge_certification() {
        return age_certification;
    }
    public void setAge_certification(String age_certification) {
        this.age_certification = age_certification;
    }

    public Integer getSeasons() {
        return seasons;
    }
    public void setSeasons(Integer seasons) {
        this.seasons = seasons;
    }
}
