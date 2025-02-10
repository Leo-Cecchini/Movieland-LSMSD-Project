package it.unipi.movieland.model.Comment;


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

    // Costruttore predefinito (senza generazione ID)
    public Comment() {
        this.datetime = LocalDateTime.now();
    }

    // Costruttore parametrizzato
    public Comment(String id, String text, String author) {
        this.id = id; // L'ID viene generato da un servizio
        this.datetime = LocalDateTime.now();
        this.text = text;
        this.author = author;
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

    @JsonIgnore
    public String getFormattedDatetime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return datetime.format(formatter);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", datetime='" + datetime + '\'' +
                ", text='" + text + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}