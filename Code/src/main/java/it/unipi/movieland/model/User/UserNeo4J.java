package it.unipi.movieland.model.User;

import it.unipi.movieland.model.CountryEnum;
import it.unipi.movieland.model.GenreEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Node("User")
public class UserNeo4J {

    @Id
    private String username;

    @Property("name")
    private String name;

    @Property("surname")
    private String surname;

    @Property("country")
    private CountryEnum country;

    @Property("favorite_genres")
    private List<GenreEnum> favoriteGenres;

    public UserNeo4J(String username, String name, String surname, CountryEnum country, List<GenreEnum> favoriteGenres) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.favoriteGenres = favoriteGenres;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public List<GenreEnum> getFavoriteGenres() {
        return favoriteGenres;
    }

    public String getUsername() {
        return username;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFavoriteGenres(List<GenreEnum> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
