package applicationMovieland.model.Celebrity;

import java.util.List;

public class Celebrity {
    private int id;
    private String name;
    private List<Job> jobs;
    private int followers;
    private String poster;

    // Costruttore
    public Celebrity(int id, String name, List<Job> jobs, int followers, String poster) {
        this.id = id;
        this.name = name;
        this.jobs = jobs;
        this.followers = followers;
        this.poster = poster;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "Celebrity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobs=" + jobs +
                ", followers=" + followers +
                ", poster='" + poster + '\'' +
                '}';
    }
}