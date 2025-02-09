package it.unipi.movieland.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Optional;

@Data
@NoArgsConstructor
public class ActorDTO {

    private int actor_id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) //ignored if poster not present
    private String poster;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) //ignore this field during queries where it's not used
    private Double averageScore;

    private int movieCount;

    public ActorDTO(int actor_id, String name, String poster, Double averageScore, int movieCount) {
        this.actor_id = actor_id;
        this.name = name;
        this.poster = poster;
        this.averageScore = averageScore;
        this.movieCount = movieCount;
    }

    public int getActor_id() {
        return actor_id;
    }
    public void setActor_id(int id) {
        this.actor_id = id;
    }

    public Optional<Double> getAverageScore() {return Optional.ofNullable(averageScore);}
    public void setAverageScore(Double averageScore) {this.averageScore = averageScore;}

    public String getPoster() {return poster;}
    public void setPoster(String poster) {this.poster = poster;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getMovieCount() {
        return movieCount;
    }
    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }
}
