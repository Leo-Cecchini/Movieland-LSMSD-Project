package it.unipi.movieland.model.Comment;
//
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Document(collection = "Comments")
public class Comment {

    @Id
    private String id;
    private LocalDateTime datetime;
    private String text;
    private String author;
    private String post_id;

    //COSTRUTTORE PREDEFINITO
    public Comment() {
        this.datetime = LocalDateTime.now();
    }

    //COSTRUTTORE PARAMETRIZZATO
    public Comment(String id, String text, String author, String post_id) {
        this.id = id; // L'ID viene generato da un servizio
        this.datetime = LocalDateTime.now();
        this.text = text;
        this.author = author;
        this.post_id = post_id;
    }

    //GETTER E SETTER
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPost_id() { return post_id; }
    public void setPost_id(String post_id) { this.post_id = post_id; }

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