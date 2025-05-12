package it.unipi.movieland.utils.deserializers;

import com.google.gson.*;
import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import it.unipi.movieland.model.Movie.MovieMongoDB;
import it.unipi.movieland.model.Enum.TitleTypeEnum;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TitleDeserializer implements JsonDeserializer<MovieMongoDB> {

    @Override
    public MovieMongoDB deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //GET THE JSON OBJECT FROM THE JSON RESPONSE
        JsonObject jsonObject = json.getAsJsonObject();

        //EXTRACT THE TITLE
        String title = getAsString(jsonObject, "title");

        //EXTRACT THE RELEASE YEAR
        Integer releaseYear = getAsInt(jsonObject, "year");

        //EXTRACT THE DESCRIPTION
        String description = getAsString(jsonObject, "description");

        //EXTRACT THE RUNTIME
        Integer runtime = getAsInt(jsonObject, "runtime");

        //EXTRACT THE IMDB ID (UNIQUE FILM ID)
        String imdbId = getNestedAsString(jsonObject, "ids", "imdb");

        //EXTRACT THE TITLE TYPE (MOVIE, TV SERIES, ETC.)
        TitleTypeEnum type = TitleTypeEnum.valueOf(Objects.requireNonNull(getAsString(jsonObject, "type")).toUpperCase());

        //EXTRACT THE IMDB SCORE (IMDB_SCORE) AND IMDB VOTES (IMDB_VOTES)
        Integer imdbScore = null;
        Integer imdbVotes = null;
        if (jsonObject.has("ratings")) {
            JsonArray ratings = jsonObject.getAsJsonArray("ratings");
            for (JsonElement ratingElement : ratings) {
                JsonObject ratingObj = ratingElement.getAsJsonObject();

                if ("imdb".equalsIgnoreCase(getAsString(ratingObj, "source"))) {
                    imdbScore = getAsInt(ratingObj, "score");
                    imdbVotes = getAsInt(ratingObj, "votes");
                    break;
                }
            }
        }

        //EXTRACT GENRES AS A LIST OF ENUMS
        List<GenreEnum> genres = new ArrayList<>();
        if (jsonObject.has("genres")) {
            JsonArray genresArray = jsonObject.getAsJsonArray("genres");
            for (JsonElement genreElement : genresArray) {

                String genreStr = getAsString(genreElement.getAsJsonObject(), "title").toUpperCase();

                if (EnumUtils.isValidEnum(GenreEnum.class, genreStr)) {
                    genres.add(GenreEnum.valueOf(genreStr));
                }
            }
        }

        List<CountryEnum> productionCountries = new ArrayList<>();
        String countryValue = getAsString(jsonObject, "country");
        if (countryValue != null) {

            productionCountries.add(CountryEnum.valueOf(countryValue.toUpperCase()));
        }

        String ageCertification = getAsString(jsonObject, "certification");

        String posterPath = getAsString(jsonObject, "poster");

        return new MovieMongoDB(title, releaseYear, description, runtime, imdbId, type, imdbScore, imdbVotes, genres, productionCountries, ageCertification, posterPath);
    }

    //UTILITY METHOD TO GET A STRING VALUE FROM A JSON OBJECT
    private String getAsString(JsonObject obj, String memberName) {

        return obj.has(memberName) && !obj.get(memberName).isJsonNull() ? obj.get(memberName).getAsString() : null;
    }

    //UTILITY METHOD TO GET AN INTEGER VALUE FROM A JSON OBJECT
    private Integer getAsInt(JsonObject obj, String memberName) {

        return obj.has(memberName) && !obj.get(memberName).isJsonNull() ? obj.get(memberName).getAsInt() : null;
    }

    //UTILITY METHOD TO GET A STRING VALUE FROM A NESTED JSON OBJECT
    private String getNestedAsString(JsonObject obj, String parentKey, String childKey) {

        if (obj.has(parentKey) && obj.get(parentKey).isJsonObject()) {
            JsonObject nestedObj = obj.getAsJsonObject(parentKey);
            return getAsString(nestedObj, childKey);
        }
        return null;
    }
}