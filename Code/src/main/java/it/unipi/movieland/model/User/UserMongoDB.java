package it.unipi.movieland.model.User;

import it.unipi.movieland.model.GenreEnum;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Document(collection = "Users")
public class UserMongoDB {
    @Id
    private String _id;
    private String email;
    private String name;
    private String surname;
    private String password;
    private String country;
    private String phone_number;
    private List<GenreEnum> favorite_genres;
    private String gender;
    private LocalDate birthday;
    private List<UserMovie> liked_movies;
    private List<UserMovie> watchlist;
    private List<UserCelebrity> followed_celebrities;
    private Integer follower;
    private Integer followed;
    private UserReview recent_review;

    public UserMongoDB(String _id, String email, String name, String surname, String password, String country,
                       String phone_number, List<GenreEnum> favorite_genres, String gender, LocalDate birthday, List<UserMovie> liked_movies, List<UserMovie> watchlist, List<UserCelebrity> followed_celebrities, Integer follower, Integer followed, UserReview recent_review) {
        this._id = _id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.country = country;
        this.phone_number = phone_number;
        this.favorite_genres = favorite_genres;
        this.gender = gender;
        this.birthday = birthday;
        this.liked_movies = liked_movies;
        this.watchlist = watchlist;
        this.followed_celebrities = followed_celebrities;
        this.follower = follower;
        this.followed = followed;
        this.recent_review = recent_review;
    }
    public UserMongoDB(String _id, String email, String name, String surname, String password, String country,
                       String phone_number, List<GenreEnum> favorite_genres, String gender, LocalDate birthday) {
        this._id = _id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.country = country;
        this.phone_number = phone_number;
        this.favorite_genres = favorite_genres;
        this.gender = gender;
        this.birthday = birthday;
        this.liked_movies = new ArrayList<>();
        this.watchlist = new ArrayList<>();
        this.followed_celebrities = new ArrayList<>();
        this.follower = 0;
        this.followed = 0;
        this.recent_review = null;
    }

    @Override
    public String toString() {
        return "User{" +
                " _id='" + _id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", country='" + country + '\'' +
                ", phoneNumber='" + phone_number + '\'' +
                ", favoriteGenres=" + favorite_genres +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", likedMovies=" + liked_movies +
                ", watchlist=" + watchlist +
                ", followedCelebrities=" + followed_celebrities +
                ", followerCount=" + follower +
                ", followedCount=" + followed +
                ", recentReview=" + recent_review +
                '}';
    }

    public String getName() {
        return name;
    }

    public Integer getFollowed() {
        return followed;
    }

    public Integer getFollower() {
        return follower;
    }

    public List<GenreEnum> getFavorite_genres() {
        return favorite_genres;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public List<UserCelebrity> getFollowed_celebrities() {
        return followed_celebrities;
    }

    public List<UserMovie> getLiked_movies() {
        return liked_movies;
    }

    public List<UserMovie> getWatchlist() {
        return watchlist;
    }

    public String get_id() {
        return _id;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getSurname() {
        return surname;
    }

    public UserReview getRecent_review() {
        return recent_review;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFavorite_genres(List<GenreEnum> favorite_genres) {
        this.favorite_genres = favorite_genres;
    }

    public void setFollowed(Integer followed) {
        this.followed = followed;
    }

    public void setFollowed_celebrities(List<UserCelebrity> followed_celebrities) {
        this.followed_celebrities = followed_celebrities;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public void setLiked_movies(List<UserMovie> liked_movies) {
        this.liked_movies = liked_movies;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setRecent_review(UserReview recent_review) {
        this.recent_review = recent_review;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setWatchlist(List<UserMovie> watchlist) {
        this.watchlist = watchlist;
    }
}