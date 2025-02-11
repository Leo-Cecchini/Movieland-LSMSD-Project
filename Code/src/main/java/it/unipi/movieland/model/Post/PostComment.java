package it.unipi.movieland.model.Post;

import java.time.LocalDateTime;

public class PostComment {

    private String comment_id;
    private LocalDateTime dateTime;

    public PostComment() {}

    public PostComment(String comment_id, LocalDateTime dateTime) {
        this.comment_id = comment_id;
        this.dateTime = dateTime;
    }

    public String getComment_id() {
        return comment_id;
    }
    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }



    @Override
    public String toString() {
        return "PostComment{" +
                "comment_id='" + comment_id + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
