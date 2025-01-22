package it.unipi.movieland.model.Movie;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document(collection = "Movies")
public class Movie {
    @Id
    private String _id;
    private String title;
    private String type;
    private String description;
    private int releaseYear;
    private Genre genres;
    private Keyword keywords;
    private ProductionCountries productionCountries;
    private int runtime;
    private String posterPath;
    private double imdbScore;
    private double imdbVotes;
    private Platform platform;
    private List<Actor> actors;
    private Reviews reviews;
    private Double revenue;
    private Double budget;
    private String ageCertification;
    private Seasons seasons;

    // Costruttore
    public Movie(String _id, String title, String type, String description, int releaseYear, Genre genres,
                 Keyword keywords, ProductionCountries productionCountries, int runtime, String posterPath,
                 double imdbScore, double imdbVotes, Platform platform, List<Actor> actors, Reviews reviews,
                 Double revenue, Double budget, String ageCertification, Seasons seasons) {
        this._id = _id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.keywords = keywords;
        this.productionCountries = productionCountries;
        this.runtime = runtime;
        this.posterPath = posterPath;
        this.imdbScore = imdbScore;
        this.imdbVotes = imdbVotes;
        this.platform = platform;
        this.actors = actors;
        this.reviews = reviews;
        this.revenue = revenue;
        this.budget = budget;
        this.ageCertification = ageCertification;
        this.seasons = seasons;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Genre getGenres() {
        return genres;
    }

    public void setGenres(Genre genres) {
        this.genres = genres;
    }

    public Keyword getKeywords() {
        return keywords;
    }

    public void setKeywords(Keyword keywords) {
        this.keywords = keywords;
    }

    public ProductionCountries getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(ProductionCountries productionCountries) {
        this.productionCountries = productionCountries;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getImdbScore() {
        return imdbScore;
    }

    public void setImdbScore(double imdbScore) {
        this.imdbScore = imdbScore;
    }

    public double getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(double imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
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

    public String getAgeCertification() {
        return ageCertification;
    }

    public void setAgeCertification(String ageCertification) {
        this.ageCertification = ageCertification;
    }

    public Seasons getSeasons() {
        return seasons;
    }

    public void setSeasons(Seasons seasons) {
        this.seasons = seasons;
    }

    // toString (utile per il debug)
    @Override
    public String toString() {
        return "Movie{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", releaseYear=" + releaseYear +
                ", genres=" + genres +
                ", keywords=" + keywords +
                ", productionCountries=" + productionCountries +
                ", runtime=" + runtime +
                ", posterPath='" + posterPath + '\'' +
                ", imdbScore=" + imdbScore +
                ", imdbVotes=" + imdbVotes +
                ", platform=" + platform +
                ", actors=" + actors +
                ", reviews=" + reviews +
                ", revenue=" + revenue +
                ", budget=" + budget +
                ", ageCertification='" + ageCertification + '\'' +
                ", seasons=" + seasons +
                '}';
    }
}


