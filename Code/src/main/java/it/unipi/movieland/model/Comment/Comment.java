package it.unipi.movieland.model.Comment;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Document(collection = "Comments")
public class Comment {

    @Id
    private String id;
    private LocalDateTime datetime;
    private String text;
    private String author;
    private ObjectId post_id;

    // Costruttore predefinito
    public Comment() {
        this.datetime = LocalDateTime.now();
    }

    // Costruttore parametrizzato
    public Comment(String text, String author, String post_id) {
        this.datetime = LocalDateTime.now();
        this.text = text;
        this.author = author;
        this.post_id = new ObjectId(post_id); // Conversione della stringa a ObjectId
    }

    // Getter e Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public ObjectId getPost_id() { return post_id; }
    public void setPost_id(ObjectId post_id) { this.post_id = post_id; }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", datetime='" + datetime + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", post_id='" + post_id + '\'' +
                '}';
    }
}