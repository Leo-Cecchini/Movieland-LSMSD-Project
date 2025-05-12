package it.unipi.movieland.dto.Celebrity;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class CommentDTO {

    private String id;
    private String author;
    private LocalDateTime datetime;
    private String text;
    private ObjectId postId;

    public CommentDTO(String id,String author, LocalDateTime datetime, String text, ObjectId postId) {
        this.id = id;
        this.author = author;
        this.datetime = datetime;
        this.text = text;
        this.postId = postId;
    }

    //GETTERS AND SETTERS
    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }

    public String getAuthor(){ return author; }
    public void setAuthor(String author){ this.author = author; }

    public LocalDateTime getDatetime(){ return datetime; }
    public void setDatetime(LocalDateTime datetime){ this.datetime = datetime; }

    public String getText(){ return text; }
    public void setText(String text){ this.text = text; }

    public ObjectId getPostId(){ return postId; }
    public void setPostId(ObjectId postId){ this.postId = postId; }
}
