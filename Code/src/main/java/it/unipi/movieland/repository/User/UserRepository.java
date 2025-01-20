package applicationMovieland.repository.User;

import applicationMovieland.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // Puoi aggiungere query personalizzate se necessario
}
