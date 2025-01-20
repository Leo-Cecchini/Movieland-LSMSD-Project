package applicationMovieland.controller.User;

import applicationMovieland.model.User.FollowedCelebrity;
import applicationMovieland.service.FollowedCelebrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/followedCelebrities")
public class FollowedCelebrityController {

    @Autowired
    private FollowedCelebrityService followedCelebrityService;

    // Aggiungi una nuova celebrità seguita
    @PostMapping
    public FollowedCelebrity followCelebrity(@RequestBody FollowedCelebrity followedCelebrity) {
        return followedCelebrityService.followCelebrity(followedCelebrity);
    }

    // Altri metodi per gestire le celebrità seguite
}