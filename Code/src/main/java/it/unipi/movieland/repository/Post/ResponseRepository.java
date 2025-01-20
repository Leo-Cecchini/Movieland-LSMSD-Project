package applicationMovieland.repository.Post;

import applicationMovieland.model.Post.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<Response, String> {

    // Puoi aggiungere metodi personalizzati per gestire le risposte
}
