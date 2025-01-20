package applicationMovieland.repository.User;

import applicationMovieland.model.User.RecentReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentReviewRepository extends JpaRepository<RecentReview, Integer> {
    // Puoi aggiungere metodi personalizzati se necessario
}