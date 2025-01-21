package it.unipi.movieland.controller.User;

import it.unipi.movieland.model.User.FollowedCelebrity;
import it.unipi.movieland.service.User.FollowedCelebrityService;
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