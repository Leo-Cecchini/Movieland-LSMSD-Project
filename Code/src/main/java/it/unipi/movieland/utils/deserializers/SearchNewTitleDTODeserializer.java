package it.unipi.movieland.utils.deserializers;

import com.google.gson.*;
import it.unipi.movieland.dto.SearchNewTitleDTO;
import it.unipi.movieland.model.TitleTypeEnum;

import java.lang.reflect.Type;

public class SearchNewTitleDTODeserializer implements JsonDeserializer<SearchNewTitleDTO> {
    @Override
    public SearchNewTitleDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String title = jsonObject.has("title") && !jsonObject.get("title").isJsonNull() ? jsonObject.get("title").getAsString() : null;
        Integer year = jsonObject.has("year") && !jsonObject.get("year").isJsonNull() ? jsonObject.get("year").getAsInt() : null;
        String type = jsonObject.has("type") && !jsonObject.get("type").isJsonNull() ? jsonObject.get("type").getAsString().toUpperCase() : null;

        // handling imdbid "ids"
        String imdbId = null;
        if (jsonObject.has("ids") && !jsonObject.getAsJsonObject("ids").isJsonNull()) {
            JsonObject ids = jsonObject.getAsJsonObject("ids");
            imdbId = ids.has("imdbid") && !ids.get("imdbid").isJsonNull() ? ids.get("imdbid").getAsString() : null;
        }
        return new SearchNewTitleDTO( imdbId, year, title, TitleTypeEnum.valueOf(type));
    }
}
