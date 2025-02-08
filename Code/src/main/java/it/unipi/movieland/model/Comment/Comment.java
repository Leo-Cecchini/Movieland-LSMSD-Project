package it.unipi.movieland.model.Comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Document(collection = "Comments")
public class Comment {

    @Id
    private String id;
    private LocalDateTime datetime;
    private String text;
    private String author;

    //GENERATORE DI ID ALFANUMERICO CASUALE
    private static String generateRandomId(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    //COSTRUTTORE PREDEFINITO
    public Comment() {
        this.id = generateRandomId(7); // Puoi rimuoverlo se lasci a MongoDB la gestione dell'ID
        this.datetime = LocalDateTime.now();
    }

    //COSTRUTTORE PARAMETRIZZATO
    public Comment(String text, String author) {
        this.id = generateRandomId(7);
        this.datetime = LocalDateTime.now();
        this.text = text;
        this.author = author;
    }

    //METODI GET E SET
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    //METODO PER OTTENERE LA DATA COME STRINGA FORMATTATA
    @JsonIgnore
    public String getFormattedDatetime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return datetime.format(formatter);
    }

    //METODO TOSTRING PER IL DEBUG
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

