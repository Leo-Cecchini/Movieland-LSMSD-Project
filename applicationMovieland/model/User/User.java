package applicationMovieland.model.User;

import java.util.Date;
import java.util.List;

public class User {
    private String id;
    private String email;
    private String name;
    private String surname;
    private String password;
    private String country;
    private String phoneNumber;
    private List<String> favoriteGenres;
    private String gender;
    private Date birthday;
    private List<LikedMovie> likedMovies;
    private List<WatchlistItem> watchlist;
    private List<FollowedCelebrity> followedCelebrities;
    private int followerCount;
    private int followedCount;
    private RecentReview recentReview;

    // Costruttore
    public User(String id, String email, String name, String surname, String password, String country,
                String phoneNumber, List<String> favoriteGenres, String gender, Date birthday,
                List<LikedMovie> likedMovies, List<WatchlistItem> watchlist,
                List<FollowedCelebrity> followedCelebrities, int followerCount, int followedCount,
                RecentReview recentReview) {
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
        this.followerCount = followerCount;
        this.followedCount = followedCount;
        this.recentReview = recentReview;
    }

    // Getter e Setter

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getFavoriteGenres() {
        return favoriteGenres;
    }

    public void setFavoriteGenres(List<String> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<LikedMovie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(List<LikedMovie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public List<WatchlistItem> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(List<WatchlistItem> watchlist) {
        this.watchlist = watchlist;
    }

    public List<FollowedCelebrity> getFollowedCelebrities() {
        return followedCelebrities;
    }

    public void setFollowedCelebrities(List<FollowedCelebrity> followedCelebrities) {
        this.followedCelebrities = followedCelebrities;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowedCount() {
        return followedCount;
    }

    public void setFollowedCount(int followedCount) {
        this.followedCount = followedCount;
    }

    public RecentReview getRecentReview() {
        return recentReview;
    }

    public void setRecentReview(RecentReview recentReview) {
        this.recentReview = recentReview;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
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
                ", followerCount=" + followerCount +
                ", followedCount=" + followedCount +
                ", recentReview=" + recentReview +
                '}';
    }
}