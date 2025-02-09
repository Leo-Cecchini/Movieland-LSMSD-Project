package it.unipi.movieland.DTO;

public class PostActivityDTO {
    private int hour;
    private int postCount;

    public PostActivityDTO(int hour, int postCount) {
        this.hour = hour;
        this.postCount = postCount;
    }

    public int getHour() {return hour;}
    public void setHour(int hour) {this.hour = hour;}

    public int getPostCount() {return postCount;}
    public void setPostCount(int postCount) {this.postCount = postCount;}
}
