package it.unipi.movieland.service.User;

import it.unipi.movieland.model.User.FollowedCelebrity;
import it.unipi.movieland.repository.User.FollowedCelebrityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowedCelebrityService {

    @Autowired
    private FollowedCelebrityRepository followedCelebrityRepository;

    // Aggiungi una nuova celebrità seguita
    public FollowedCelebrity followCelebrity(FollowedCelebrity celebrity) {
        return followedCelebrityRepository.save(celebrity);
    }

    // Altri metodi per la gestione delle celebrità
}