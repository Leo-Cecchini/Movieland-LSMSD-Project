package it.unipi.movieland.dto;

import it.unipi.movieland.dto.Celebrity.CelebrityMongoDto;
import it.unipi.movieland.model.Celebrity.CelebrityMongoDB;
import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.Enum.TitleTypeEnum;
import it.unipi.movieland.model.Movie.MovieMongoDB;

import java.util.List;

public class MovieMongoDTO {
    private String id;
    private String title;
    private TitleTypeEnum type;
    private String description;
    private Integer releaseYear;
    private Integer runtime;
    private Integer imdbScore;
    private String posterPath;
    private String ageCertification;
    private List<CountryEnum> productionCountries;
    private List<String> platform;
    private List<GenreEnum> genres;

    public MovieMongoDTO(MovieMongoDB movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.type = movie.getType();
        this.description = movie.getDescription();
        this.releaseYear = movie.getReleaseYear();
        this.runtime = movie.getRuntime();
        this.imdbScore = movie.getImdbScore();
        this.posterPath = movie.getPosterPath();
        this.ageCertification = movie.getAgeCertification();
        this.productionCountries = movie.getProductionCountries();
        this.platform = movie.getPlatform();
        this.genres = movie.getGenres();
    }

    //GETTERS E SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id;}

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

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getAgeCertification() { return ageCertification; }
    public void setAgeCertification(String ageCertification) { this.ageCertification = ageCertification; }

    public List<CountryEnum> getProductionCountries() { return productionCountries; }
    public void setProductionCountries(List<CountryEnum> productionCountries) { this.productionCountries = productionCountries; }

    public List<String> getPlatform() { return platform; }
    public void setPlatform(List<String> platform) { this.platform = platform; }

    public List<GenreEnum> getGenres() { return genres; }
    public void setGenres(List<GenreEnum> genres) { this.genres = genres; }

    public static MovieMongoDTO fromEntity(MovieMongoDB entity) {
        return new MovieMongoDTO(entity);
    }
}
