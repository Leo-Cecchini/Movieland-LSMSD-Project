package it.unipi.movieland.model.Comment;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "Comments")
public class CommentMongoDB {

    @Id
    private String id;
    private String author;
    private LocalDateTime datetime;
    private String text;

    @Field("post_id")
    private ObjectId postId;

    public CommentMongoDB() {
        this.datetime = LocalDateTime.now();
    }

    public CommentMongoDB(String text, String author, String postId) {
        this.datetime = LocalDateTime.now();
        this.text = text;
        this.author = author;
        this.postId = new ObjectId(postId);
    }

    //GETTERS AND SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public ObjectId getPostId() { return postId; }
    public void setPostId(ObjectId postId) { this.postId = postId; }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", datetime='" + datetime + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", post id='" + postId + '\'' +
                '}';
    }
}