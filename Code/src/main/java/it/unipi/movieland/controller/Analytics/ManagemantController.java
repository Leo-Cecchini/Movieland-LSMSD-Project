package it.unipi.movieland.controller.Analytics;

import it.unipi.movieland.service.Celebrity.CelebrityService;
import it.unipi.movieland.service.Movie.MovieService;
import it.unipi.movieland.service.Review.ReviewService;
import it.unipi.movieland.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("manage")
public class ManagemantController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;
    @Autowired
    private CelebrityService celebrityService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/user/mongo")
    public List<String> userInconsistenciesMongo() {
        return userService.inconsistenciesMongo();
    }
    @GetMapping("/user/neo")
    public List<String> userInconsistenciesNeo() {
        return userService.inconsistenciesNeo();
    }

    @GetMapping("/review/mongo")
    public List<String> reviewInconsistenciesMongo() {
        return reviewService.inconsistenciesMongo();
    }
    @GetMapping("/review/neo")
    public List<String> reviewInconsistenciesNeo() {
        return reviewService.inconsistenciesNeo();
    }

    @GetMapping("/celebrity/mongo")
    public List<String> celebrityInconsistenciesMongo() {
        return celebrityService.inconsistenciesMongo();
    }
    @GetMapping("/celebrity/neo")
    public List<String> celebrityInconsistenciesNeo() {
        return celebrityService.inconsistenciesNeo();
    }

    @GetMapping("/movie/mongo")
    public List<String> movieInconsistenciesMongo() {
        return movieService.inconsistenciesMongo();
    }
    @GetMapping("/movie/neo")
    public List<String> movieInconsistenciesNeo() {
        return movieService.inconsistenciesNeo();
    }
}
