package applicationMovieland.model.Celebrity;

public class Job {
    private String role;
    private String movieId;
    private String movieTitle;
    private String character;

    // Costruttore
    public Job(String role, String movieId, String movieTitle, String character) {
        this.role = role;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.character = character;
    }

    // Getter e Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    // toString (opzionale)
    @Override
    public String toString() {
        return "Job{" +
                "role='" + role + '\'' +
                ", movieId='" + movieId + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", character='" + character + '\'' +
                '}';
    }
}
