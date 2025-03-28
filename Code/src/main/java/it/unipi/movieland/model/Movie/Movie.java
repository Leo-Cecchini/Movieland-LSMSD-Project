package it.unipi.movieland.model.Movie;

import it.unipi.movieland.model.CountryEnum;
import it.unipi.movieland.model.GenreEnum;
import it.unipi.movieland.model.PlatformEnum;
import it.unipi.movieland.model.TitleTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Movies")
public class Movie {
    @Id
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
    private Integer imdb_score;
    private Integer imdb_votes;
    private List<String> platform;
    private List<MovieCelebrity> actors;
    private List<MovieCelebrity> directors;
    private List<MovieReview> reviews;
    private Double revenue;
    private Double budget;
    private String age_certification;
    private Integer seasons;

    //empty constructor
    public Movie() {}

    // constructor for creating the title from the json object return by the mdblist api
    public Movie(String title, Integer release_year, String description, Integer runtime, String _id, TitleTypeEnum type,
                 Integer imdb_score, Integer imdb_votes, List<GenreEnum> genres, List<CountryEnum> production_countries,
                 String age_certification, String poster_path) {
        this.title = title;
        this.release_year = release_year;
        this.description = description;
        this.runtime = runtime;
        this._id = _id;
        this.type = type;
        this.imdb_score = imdb_score;
        this.imdb_votes = imdb_votes;
        this.genres = genres;
        this.production_countries = production_countries;
        this.age_certification = age_certification;
        this.poster_path = poster_path;
        this.reviews = new ArrayList<>();
        this.keywords = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.directors = new ArrayList<>();
        this.revenue = null;
        this.budget = null;
        this.seasons = null;
    }

    // constructor
    public Movie(String _id, String title, TitleTypeEnum type, String description, Integer release_year, List<GenreEnum> genres,
                 List<String> keywords, List<CountryEnum> production_countries, Integer runtime, String poster_path,
                 Integer imdb_score, Integer imdb_votes, List<String> platform, List<MovieCelebrity> actors, List<MovieCelebrity> directors,
                 Double revenue, Double budget, String age_certification, Integer seasons,List<MovieReview> reviews) {
        this._id = _id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.release_year = release_year;
        this.genres = genres;
        this.keywords = keywords;
        this.production_countries = production_countries;
        this.runtime = runtime;
        this.poster_path = poster_path;
        this.imdb_score = imdb_score;
        this.imdb_votes = imdb_votes;
        this.platform = platform;
        this.actors = actors;
        this.directors = directors;
        this.revenue = revenue;
        this.budget = budget;
        this.age_certification = age_certification;
        this.seasons = seasons;
        this.reviews = reviews;
    }

    // Getter e Setter
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

    public TitleTypeEnum getType() {
        return type;
    }
    public void setType(TitleTypeEnum type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getrelease_year() {
        return release_year;
    }
    public void setrelease_year(Integer release_year) {
        this.release_year = release_year;
    }

    public List<GenreEnum> getGenre() {
        return genres;
    }
    public void setGenre(List<GenreEnum> genre) {
        this.genres = genre;
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
    public void setProduction_countries(List<CountryEnum> production_countries) {this.production_countries = production_countries;}

    public int getRuntime() {
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

    public double getimdb_score() {
        return imdb_score;
    }
    public void setimdb_score(Integer imdb_score) {
        this.imdb_score = imdb_score;
    }

    public double getimdb_votes() {
        return imdb_votes;
    }
    public void setimdb_votes(Integer imdb_votes) {
        this.imdb_votes = imdb_votes;
    }

    public List<String> getPlatform() {
        return platform;
    }
    public void setPlatform(List<String> platform) {
        this.platform = platform;
    }

    public List<MovieCelebrity> getActors() {
        return actors;
    }
    public void setActors(List<MovieCelebrity> actors) {
        this.actors = actors;
    }

    public List<MovieCelebrity> getDirectors() {return directors;}
    public void setDirectors(List<MovieCelebrity> directors) {this.directors = directors;}

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

    public String getage_certification() {
        return age_certification;
    }
    public void setage_certification(String age_certification) {
        this.age_certification = age_certification;
    }

    public Integer getSeasons() {
        return seasons;
    }
    public void setSeasons(Integer seasons) {
        this.seasons = seasons;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }

    // toString
    @Override
    public String toString() {
        return "Movie{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", release_year=" + release_year +
                ", genres=" + genres +
                ", keywords=" + keywords +
                ", production_countries=" + production_countries +
                ", runtime=" + runtime +
                ", poster_path='" + poster_path + '\'' +
                ", imdb_score=" + imdb_score +
                ", imdb_votes=" + imdb_votes +
                ", platform=" + platform +
                ", actors=" + actors +
                ", directors=" + directors +
                ", revenue=" + revenue +
                ", budget=" + budget +
                ", age_certification='" + age_certification + '\'' +
                ", seasons=" + seasons +
                '}';
    }
}


