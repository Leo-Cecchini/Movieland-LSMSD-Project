package it.unipi.movieland.controller.Celebrity;

import it.unipi.movieland.exception.CelebrityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import it.unipi.movieland.service.Celebrity.CelebrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import it.unipi.movieland.dto.Celebrity.CelebrityMongoDto;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/celebrities")
public class CelebrityController {

    private final CelebrityService celebrityService;

    @Autowired
    public CelebrityController(CelebrityService celebrityService) {
        this.celebrityService = celebrityService;
    }

    //GET CELEBRITIES WITH PAGINATION SUPPORT, DEFAULT: PAGE=0, SIZE=20
    @GetMapping("/")
    public ResponseEntity<Page<CelebrityMongoDto>> getAllCelebritiesMongo(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Max(100) int size) {

        Page<CelebrityMongoDto> result = celebrityService.getAllCelebritiesMongo(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    //GET CELEBRITY BY ID
    @GetMapping("/{celebrityId}")
    public ResponseEntity<Object> getCelebrityByIdMongo(@PathVariable int celebrityId) {
        try {
            CelebrityMongoDto celebrityDto = celebrityService.getCelebrityByIdMongo(celebrityId);
            return ResponseEntity.ok(celebrityDto);
        } catch (CelebrityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //SEARCH CELEBRITIES BY KEYWORD OR NAME
    @GetMapping("/search")
    public ResponseEntity<Object> searchMongo(@RequestParam String text) {
        try
        {
            List<CelebrityMongoDto> celebrities = celebrityService.searchActorsByCharacterMongo(text);
            return ResponseEntity.ok(celebrities);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    //CREATE A NEW CELEBRITY
    @PostMapping("/")
    public ResponseEntity<Object> createCelebrity(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam (required = false) String poster) {
        try {

            String responseMessage = celebrityService.createCelebrityInBothDatabases(id, name, poster);

            Map<String, Object> response = new HashMap<>();
            response.put("message", responseMessage);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO CREATE CELEBRITY", "details", e.getMessage()));
        }
    }

    //DELETE CELEBRITY BY ID
    @DeleteMapping("/delete/{celebrityId}")
    public ResponseEntity<Object> deleteCelebrityById(@PathVariable int celebrityId) {
        try {
            celebrityService.deleteCelebrityInBothDatabases(celebrityId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "CELEBRITY WITH ID " + celebrityId + " HAS BEEN DELETED."));

        } catch (CelebrityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO DELETE CELEBRITY.", "details", e.getMessage()));
        }
    }

    //ADD JOB AS ACTOR TO A CELEBRITY
    @PostMapping("/{celebrityId}/jobs/actor")
    public ResponseEntity<Object> updateJobToActor(
            @PathVariable int celebrityId,
            @RequestParam String movie_id,
            @RequestParam String character) {

        return celebrityService.addJobToActor(celebrityId, movie_id, character);
    }

    //ADD JOB AS DIRECTOR TO A CELEBRITY
    @PostMapping("/{celebrityId}/jobs/director")
    public ResponseEntity<Object> updateJobToDirector(
            @PathVariable int celebrityId,
            @RequestParam String movie_id) {

        return celebrityService.addJobToDirector(celebrityId, movie_id);
    }

    //REMOVE JOB FOR A CELEBRITY
    @DeleteMapping("/{celebrityId}/jobs/{jobId}")
    public ResponseEntity<Object> deleteJobById(
            @PathVariable int celebrityId,
            @PathVariable String jobId) {

        return celebrityService.removeJobById(celebrityId, jobId);
    }

    //GET JOBS FOR A CELEBRITY
    @GetMapping("/{celebrityId}/jobs")
    public ResponseEntity<Object> getJobsForCelebrity(@PathVariable int celebrityId) {
        return celebrityService.getJobsForCelebrity(celebrityId);
    }

    //UPDATE CELEBRITY DETAILS
    @PutMapping("/update/{celebrityId}")
    public ResponseEntity<Object> updateCelebrity(
            @PathVariable String celebrityId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String poster) {
        return celebrityService.updateCelebrity(celebrityId, name, poster);
    }
}