package it.unipi.movieland.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

//user for retrieving few information about titles searched and not the whole document

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchTitleDTO {
    @JsonProperty("_id")
    private String _id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("release_year")
    private Integer release_year;

    @JsonProperty("poster_path")
    private String poster_path;

    @JsonProperty("imdb_score")
    private Integer imdb_score;

}
