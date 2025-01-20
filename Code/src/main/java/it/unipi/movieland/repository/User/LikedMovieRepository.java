package applicationMovieland.repository.User;

import applicationMovieland.model.User.LikedMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedMovieRepository extends JpaRepository<LikedMovie, String> {
    // Query personalizzate
}
