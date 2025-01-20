package applicationMovieland.repository.Post;

import applicationMovieland.model.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    // Puoi aggiungere metodi personalizzati, ad esempio per ottenere i post per movieId
    List<Post> findByMovieId(String movieId);

    // Aggiungi altri metodi personalizzati se necessario
}