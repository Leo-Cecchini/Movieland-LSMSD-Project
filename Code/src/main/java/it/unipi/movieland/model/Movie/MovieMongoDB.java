package it.unipi.movieland.model.Movie;

import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.Enum.TitleTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Movies")
public class MovieMongoDB {

    @Id
    @Field("_id")
    private String id;

    private String title;
    private TitleTypeEnum type;
    private String description;

    @Field("release_year")
    private Integer releaseYear;

    private Integer runtime;

    @Field("imdb_score")
    private Integer imdbScore;

    @Field("imdb_votes")
    private Integer imdbVotes;

    @Field("poster_path")
    private String posterPath;

    @Field("age_certification")
    private String ageCertification;

    @Field("production_countries")
    private List<CountryEnum> productionCountries = new ArrayList<>();

    private List<String> platform = new ArrayList<>();

    private List<MovieCelebrity> actors = new ArrayList<>();
    private List<MovieCelebrity> directors = new ArrayList<>();

    private List<MovieReview> reviews = new ArrayList<>();

    private List<GenreEnum> genres = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();

    private Double revenue;
    private Double budget;
    private Integer seasons;

    public MovieMongoDB() {}

    public MovieMongoDB(String title, Integer releaseYear, String description, Integer runtime, String id, TitleTypeEnum type,
                        Integer imdbScore, Integer imdbVotes, List<GenreEnum> genres, List<CountryEnum> productionCountries,
                        String ageCertification, String posterPath) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.description = description;
        this.runtime = runtime;
        this.id = id;
        this.type = type;
        this.imdbScore = imdbScore;
        this.imdbVotes = imdbVotes;
        this.genres = genres;
        this.productionCountries = productionCountries;
        this.ageCertification = ageCertification;
        this.posterPath = posterPath;
    }

    public MovieMongoDB(String id, String title, TitleTypeEnum type, String description, Integer releaseYear,
                        Integer runtime, Integer imdbScore, Integer imdbVotes, String posterPath, String ageCertification,
                        List<CountryEnum> productionCountries, List<String> platform, List<MovieCelebrity> actors,
                        List<MovieCelebrity> directors, List<MovieReview> reviews, List<GenreEnum> genres,
                        List<String> keywords, Double revenue, Double budget, Integer seasons) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
        this.imdbScore = imdbScore;
        this.imdbVotes = imdbVotes;
        this.posterPath = posterPath;
        this.ageCertification = ageCertification;
        this.productionCountries = productionCountries;
        this.platform = platform;
        this.actors = actors;
        this.directors = directors;
        this.reviews = reviews;
        this.genres = genres;
        this.keywords = keywords;
        this.revenue = revenue;
        this.budget = budget;
        this.seasons = seasons;
    }

    //GETTERS AND SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public TitleTypeEnum getType() { return type; }
    public void setType(TitleTypeEnum type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Integer getRuntime() { return runtime; }
    public void setRuntime(Integer runtime) { this.runtime = runtime; }

    public Integer getImdbScore() { return imdbScore; }
    public void setImdbScore(Integer imdbScore) { this.imdbScore = imdbScore; }

    public Integer getImdbVotes() { return imdbVotes; }
    public void setImdbVotes(Integer imdbVotes) { this.imdbVotes = imdbVotes; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getAgeCertification() { return ageCertification; }
    public void setAgeCertification(String ageCertification) { this.ageCertification = ageCertification; }

    public List<CountryEnum> getProductionCountries() { return productionCountries; }
    public void setProductionCountries(List<CountryEnum> productionCountries) { this.productionCountries = productionCountries; }

    public List<String> getPlatform() { return platform; }
    public void setPlatform(List<String> platform) { this.platform = platform; }

    public List<MovieCelebrity> getActors() { return actors; }
    public void setActors(List<MovieCelebrity> actors) { this.actors = actors; }

    public List<MovieCelebrity> getDirectors() { return directors; }
    public void setDirectors(List<MovieCelebrity> directors) { this.directors = directors; }

    public List<MovieReview> getReviews() { return reviews; }
    public void setReviews(List<MovieReview> reviews) { this.reviews = reviews; }

    public List<GenreEnum> getGenres() { return genres; }
    public void setGenres(List<GenreEnum> genres) { this.genres = genres; }

    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }

    public Double getRevenue() { return revenue; }
    public void setRevenue(Double revenue) { this.revenue = revenue; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public Integer getSeasons() { return seasons; }
    public void setSeasons(Integer seasons) { this.seasons = seasons; }

    @Override
    public String toString() {
        return "Movie{" +
                "id= " + id + '\'' +
                ", title= " + title + '\'' +
                ", type= " + type +
                ", releaseYear= " + releaseYear +
                ", imdbScore= " + imdbScore +
                ", revenue= " + revenue +
                ", budget= " + budget +
                '}';
    }
}