package it.unipi.movieland.applicationMovieland.model.User;

public class WatchlistItem {
    private String filmId;
    private String title;
    private String poster;

    public WatchlistItem(String filmId, String title, String poster) {
        this.filmId = filmId;
        this.title = title;
        this.poster = poster;
    }

    // Getter e Setter

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "WatchlistItem{" +
                "filmId=" + filmId +
                "title=" + title +
                "poster=" + poster +
                '}';
    }
}
