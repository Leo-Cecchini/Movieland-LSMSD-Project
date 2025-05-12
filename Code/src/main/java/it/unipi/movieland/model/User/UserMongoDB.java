package it.unipi.movieland.model.User;

import it.unipi.movieland.model.Enum.CountryEnum;
import it.unipi.movieland.model.Enum.GenderEnum;
import it.unipi.movieland.model.Enum.GenreEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Users")
public class UserMongoDB {
    @Id
    @Field("_id")
    private String id;

    private String email;
    private String name;
    private String surname;
    private String password;
    private CountryEnum country;

    @Field("phone_number")
    private String phoneNumber;

    @Field("favorite_genres")
    private List<GenreEnum> favoriteGenres;

    private GenderEnum gender;
    private LocalDate birthday;
    
    @Field("liked_movies")
    private List<UserMovie> likedMovies;

    private List<UserMovie> watchlist;

    @Field("followed_celebrities")
    private List<UserCelebrity> followedCelebrities;

    private Integer follower;
    private Integer followed;

    @Field("recent_review")
    private UserReview recentReview;

    public UserMongoDB(String id,String email, String name, String surname, String password, CountryEnum country,
                       String phoneNumber, List<GenreEnum> favoriteGenres, GenderEnum gender, LocalDate birthday,
                       List<UserMovie> likedMovies, List<UserMovie> watchlist, List<UserCelebrity> followedCelebrities,
                       Integer follower, Integer followed, UserReview recentReview) {

        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.favoriteGenres = favoriteGenres;
        this.gender = gender;
        this.birthday = birthday;
        this.likedMovies = likedMovies;
        this.watchlist = watchlist;
        this.followedCelebrities = followedCelebrities;
        this.follower = follower;
        this.followed = followed;
        this.recentReview = recentReview;
    }
    public UserMongoDB() {}

    public UserMongoDB(String id,String email, String name, String surname, String password, CountryEnum country,
                       String phoneNumber, List<GenreEnum> favoriteGenres, GenderEnum gender, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.favoriteGenres = favoriteGenres;
        this.gender = gender;
        this.birthday = birthday;
        this.likedMovies = new ArrayList<>();
        this.watchlist = new ArrayList<>();
        this.followedCelebrities = new ArrayList<>();
        this.follower = 0;
        this.followed = 0;
        this.recentReview = null;
    }

    //GETTERS AND SETTERS
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public CountryEnum getCountry() {
        return country;
    }
    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<GenreEnum> getFavoriteGenres() { return favoriteGenres; }
    public void setFavoriteGenres(List<GenreEnum> favoriteGenres) { this.favoriteGenres = favoriteGenres; }

    public GenderEnum getGender() {
        return gender;
    }
    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<UserMovie> getLikedMovies() {
        return likedMovies;
    }
    public void setLikedMovies(List<UserMovie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public List<UserMovie> getWatchlist() {
        return watchlist;
    }
    public void setWatchlist(List<UserMovie> watchlist) {
        this.watchlist = watchlist;
    }

    public List<UserCelebrity> getFollowedCelebrities() {
        return followedCelebrities;
    }
    public void setFollowedCelebrities(List<UserCelebrity> followedCelebrities) { this.followedCelebrities = followedCelebrities; }

    public Integer getFollower() {
        return follower;
    }
    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public void setFollowed(Integer followed) {
        this.followed = followed;
    }
    public Integer getFollowed() {
        return followed;
    }

    public UserReview getRecentReview() {
        return recentReview;
    }
    public void setRecentReview(UserReview recentReview) {
        this.recentReview = recentReview;
    }

    @Override
    public String toString() {
        return "User{" +
                " id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", country='" + country + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", favoriteGenres=" + favoriteGenres +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", likedMovies=" + likedMovies +
                ", watchlist=" + watchlist +
                ", followedCelebrities=" + followedCelebrities +
                ", followerCount=" + follower +
                ", followedCount=" + followed +
                ", recentReview=" + recentReview +
                '}';
    }
}