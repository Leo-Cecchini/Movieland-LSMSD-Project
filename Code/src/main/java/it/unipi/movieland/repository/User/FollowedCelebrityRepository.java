package applicationMovieland.repository.User;

import applicationMovieland.model.User.FollowedCelebrity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowedCelebrityRepository extends JpaRepository<FollowedCelebrity, Integer> {
    // Query personalizzate
}