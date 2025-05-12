package it.unipi.movieland.service.Celebrity;

import it.unipi.movieland.model.Movie.MovieCelebrity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unipi.movieland.dto.Celebrity.*;
import it.unipi.movieland.exception.*;
import it.unipi.movieland.model.Celebrity.CelebrityMongoDB;
import it.unipi.movieland.model.Celebrity.CelebrityNeo4J;
import it.unipi.movieland.model.Celebrity.Job;
import it.unipi.movieland.model.Movie.MovieMongoDB;
import it.unipi.movieland.repository.Celebrity.CelebrityMongoDBRepository;
import it.unipi.movieland.repository.Celebrity.CelebrityNeo4JRepository;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import it.unipi.movieland.dto.Celebrity.CelebrityMongoDto;

import java.util.*;
import java.util.stream.Collectors;

import static it.unipi.movieland.service.User.UserService.findDifference;

@Service
public class CelebrityService {

    private final CelebrityMongoDBRepository celebrityMongoRepository;
    private final CelebrityNeo4JRepository celebrityNeo4JRepository;
    private final MovieMongoDBRepository movieMongoDBRepository;

    @Autowired
    public CelebrityService(CelebrityMongoDBRepository mongoRepository, CelebrityNeo4JRepository neo4jRepository,MovieMongoDBRepository movieMongoDBRepository ) {
        this.celebrityMongoRepository = mongoRepository;
        this.celebrityNeo4JRepository = neo4jRepository;
        this.movieMongoDBRepository = movieMongoDBRepository;
    }

    @Transactional
    public void createCelebrity(CelebrityMongoDB mongoCelebrity, CelebrityNeo4J neo4jCelebrity) {
        celebrityMongoRepository.save(mongoCelebrity);
        celebrityNeo4JRepository.save(neo4jCelebrity);
    }

    //METHOD TO RETRIEVE ALL CELEBRITIES
    public Page<CelebrityMongoDto> getAllCelebritiesMongo(Pageable pageable) {

        Page<CelebrityMongoDB> list = celebrityMongoRepository.findAll(pageable);
        List<CelebrityMongoDto> dtoList = list.getContent().stream()
                .map(CelebrityMongoDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, list.getTotalElements());
    }

    //METHOD TO RETRIEVE A CELEBRITY USING THEIR ID
    public CelebrityMongoDto getCelebrityByIdMongo(int id) {

        CelebrityMongoDB celebrity = celebrityMongoRepository.findById(id)
                .orElseThrow(() -> new CelebrityNotFoundException("CELEBRITY WITH ID " + id + " NOT FOUND."));

        return CelebrityMongoDto.fromEntity(celebrity);
    }

    //METHOD TO RETRIEVE A CELEBRITY USING THEIR ID (NEO4J)
    public CelebrityNeo4JDto getCelebrityByIdNeo4j(String personId) {

        CelebrityNeo4J celebrity = celebrityNeo4JRepository.findById(personId)
                .orElseThrow(() -> new CelebrityNotFoundException("CELEBRITY WITH ID " + personId + " NOT FOUND."));

        return CelebrityNeo4JDto.fromEntity(celebrity);
    }

    //METHOD TO SEARCH FOR AN ACTOR BY NAME OR CHARACTER (MONGODB)
    public List<CelebrityMongoDto> searchActorsByCharacterMongo(String searchTerm) {
        List<CelebrityMongoDB> celebrities = celebrityMongoRepository.searchActorsAndCharacters(searchTerm);

        if (celebrities.isEmpty()) {
            throw new RuntimeException("NO CELEBRITIES FOUND FOR THE SEARCH TERM: " + searchTerm);
        }

        return celebrities.stream()
                .map(CelebrityMongoDto::fromEntity)
                .collect(Collectors.toList());
    }

    //METHOD TO CREATE A CELEBRITY IN BOTH DATABASES
    public String createCelebrityInBothDatabases(int id, String name, String poster) {
        try {
            CelebrityMongoDto existingMongoCelebrity = null;
            CelebrityNeo4JDto existingNeo4jCelebrity = null;

            try {
                existingMongoCelebrity = getCelebrityByIdMongo(id);
            } catch (CelebrityNotFoundException e) { }

            try {
                existingNeo4jCelebrity = getCelebrityByIdNeo4j(String.valueOf(id));
            } catch (CelebrityNotFoundException e) { }

            if (existingMongoCelebrity != null && existingNeo4jCelebrity != null) {
                return "CELEBRITY WITH ID " + id + " ALREADY EXISTS.";
            }

            CelebrityMongoDB newMongoCelebrity = new CelebrityMongoDB(id, name, poster);
            CelebrityMongoDB savedMongoCelebrity = celebrityMongoRepository.save(newMongoCelebrity);

            CelebrityNeo4J newNeo4jCelebrity = new CelebrityNeo4J(String.valueOf(savedMongoCelebrity.getId()), name, poster);
            celebrityNeo4JRepository.save(newNeo4jCelebrity);

            return "CELEBRITY CREATED SUCCESSFULLY IN BOTH DATABASES";

        } catch (Exception e) {
            throw new RuntimeException("ERROR CREATING CELEBRITY IN BOTH DATABASES: " + e.getMessage());
        }
    }

    //METHOD TO DELETE A CELEBRITY IN BOTH DATABASES
    @Transactional
    public void deleteCelebrityInBothDatabases(int id) {

        Optional<CelebrityMongoDB> mongoCelebrity = celebrityMongoRepository.findById(id);
        if (mongoCelebrity.isEmpty()) {
            throw new CelebrityNotFoundException("CELEBRITY WITH ID " + id + " NOT FOUND.");
        }
        celebrityMongoRepository.deleteById(id);

        Optional<CelebrityNeo4J> neo4jCelebrity = celebrityNeo4JRepository.findById(String.valueOf(id));
        if (neo4jCelebrity.isEmpty()) {
            throw new CelebrityNotFoundException("CELEBRITY WITH ID " + id + " NOT FOUND.");
        }
        celebrityNeo4JRepository.deleteById(String.valueOf(id));
    }

    //METHOD TO CREATE ID FOR THE JOB
    private String generateJobIdForCelebrity(CelebrityMongoDB celebrity) {
        if (celebrity.getJobs() == null || celebrity.getJobs().isEmpty()) {
            return celebrity.getId() + "_job1";
        }

        Set<Integer> existingJobNumbers = celebrity.getJobs().stream()
                .map(job -> {
                    String[] parts = job.getJobId().split("_job");
                    return (parts.length == 2) ? Integer.parseInt(parts[1]) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int newJobNumber = 1;
        while (existingJobNumbers.contains(newJobNumber)) {
            newJobNumber++;
        }
        return celebrity.getId() + "_job" + newJobNumber;
    }

    //METHOD TO ADD JOBS TO AN ACTOR
    @Transactional
    public ResponseEntity<Object> addJobToActor(int id, String movieId, String character) {
        try {
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(id)
                    .orElseThrow(() -> new CelebrityNotFoundException("CELEBRITY WITH ID " + id + " NOT FOUND"));

            MovieMongoDB movie = movieMongoDBRepository.findById(movieId)
                    .orElseThrow(() -> new MovieNotFoundInMongoException("MOVIE WITH ID " + movieId + " NOT FOUND"));

            if (celebrity.getJobs() == null) { celebrity.setJobs(new ArrayList<>()); }

            boolean jobExists = celebrity.getJobs().stream()
                    .anyMatch(job -> movieId.equals(job.getMovieId()) && character != null && character.equals(job.getCharacter()));
            if (jobExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "JOB ALREADY EXISTS FOR MOVIE ID: " + movieId + " AND CHARACTER: " + character));
            }
//FINO A QUA OK
            String jobId = generateJobIdForCelebrity(celebrity);
            Job newJob = new Job(celebrity, "ACTOR", movieId, movie.getTitle(), character);
            newJob.setJobId(jobId);
            celebrity.getJobs().add(newJob);
            celebrityMongoRepository.save(celebrity);

            celebrityNeo4JRepository.addActedInRelationship(String.valueOf(id), movieId, character);

            if (movie.getActors() == null) { movie.setActors(new ArrayList<>());}

            if (movie.getActors().size() >= 5) {
                return ResponseEntity.status(HttpStatus.OK) .body(Map.of("message", "JOB ADDED SUCCESSFULLY FOR ACTOR"));}

            boolean actorExists = movie.getActors().stream().anyMatch(actor -> actor.getId().equals(id));
            if (!actorExists) {
                movie.getActors().add(new MovieCelebrity(id, celebrity.getName(), "ACTOR"));
                movieMongoDBRepository.save(movie);
            }

            JobDto jobDto = JobDto.fromEntity(newJob);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "JOB ADDED SUCCESSFULLY FOR ACTOR WITH ID " + id, "job", jobDto));

        } catch (CelebrityNotFoundException | MovieNotFoundInMongoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO ADD JOB FOR ACTOR", "details", e.getMessage()));
        }
    }

    //METHOD TO ADD JOBS TO AN DIRECTOR
    @Transactional
    public ResponseEntity<Object> addJobToDirector(int id, String movie_id) {
        try {
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("CELEBRITY WITH ID " + id + " NOT FOUND"));

            MovieMongoDB movie = movieMongoDBRepository.findById(movie_id)
                    .orElseThrow(() -> new IllegalArgumentException("MOVIE WITH ID " + movie_id + " NOT FOUND"));

            if (celebrity.getJobs() == null) {
                celebrity.setJobs(new ArrayList<>());
            }

            boolean jobAlreadyExists = celebrity.getJobs().stream()
                    .anyMatch(job -> job.getMovieId().equals(movie_id) && job.getRole().equalsIgnoreCase("Director"));

            if (jobAlreadyExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "JOB ALREADY EXISTS FOR MOVIE ID: " + movie_id + " AND ROLE: DIRECTOR."));
            }

            String jobId = generateJobIdForCelebrity(celebrity);
            Job newJob = new Job();
            newJob.setJobId(jobId);
            newJob.setRole("DIRECTOR");
            newJob.setMovieId(movie_id);
            newJob.setMovieTitle(movie.getTitle());

            celebrity.getJobs().add(newJob);
            celebrityMongoRepository.save(celebrity);

            celebrityNeo4JRepository.addDirectorInRelationship(String.valueOf(id), movie_id);

            if (movie.getDirectors() == null) {
                movie.setDirectors(new ArrayList<>());
            }

            if (movie.getDirectors().size() >= 5) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "JOB ADDED SUCCESSFULLY FOR DIRECTOR"));
            }

            boolean directorExists = movie.getDirectors().stream()
                    .anyMatch(director -> director.getId().equals(id));
            if (!directorExists) {
                movie.getDirectors().add(new MovieCelebrity(id, celebrity.getName(), "Director"));
                movieMongoDBRepository.save(movie);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "JOB ADDED SUCCESSFULLY TO CELEBRITY WITH ID: " + id, "jobId", jobId));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "AN ERROR OCCURRED WHILE ADDING JOB", "details", e.getMessage()));
        }
    }

    //METHOD TO REMOVE JOBS TO ACTOR O DIRECTOR
    @Transactional
    public ResponseEntity<Object> removeJobById(int celebrityId, String jobId) {
        try {
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(celebrityId)
                    .orElseThrow(() -> new CelebrityNotFoundException("CELEBRITY WITH ID " + celebrityId + " NOT FOUND!"));

            List<Job> jobs = Optional.ofNullable(celebrity.getJobs()).orElse(Collections.emptyList());


            Job jobToRemove = jobs.stream()
                    .filter(job -> jobId.equals(job.getJobId()))
                    .findFirst()
                    .orElse(null);

            if (jobToRemove == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "JOB WITH ID " + jobId + " NOT FOUND FOR CELEBRITY WITH ID " + celebrityId));
            }

            jobs.remove(jobToRemove);
            celebrityMongoRepository.save(celebrity);

            switch (jobToRemove.getRole()) {
                case "ACTOR":
                    celebrityNeo4JRepository.removeActedInRelationship(String.valueOf(celebrityId), jobToRemove.getMovieId());
                    break;
                case "DIRECTOR":
                    celebrityNeo4JRepository.removeDirectedInRelationship(String.valueOf(celebrityId), jobToRemove.getMovieId());
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("message", "INVALID ROLE: " + jobToRemove.getRole()));
            }

            MovieMongoDB movie = movieMongoDBRepository.findById(jobToRemove.getMovieId())
                    .orElseThrow(() -> new MovieNotFoundInMongoException("MOVIE WITH ID " + jobToRemove.getMovieId() + " NOT FOUND"));

            if ("ACTOR".equalsIgnoreCase(jobToRemove.getRole())) {
                movie.getActors().removeIf(actor -> actor.getId().equals(celebrityId));
            } else if ("DIRECTOR".equalsIgnoreCase(jobToRemove.getRole())) {
                movie.getDirectors().removeIf(director -> director.getId().equals(celebrityId));
            }

            movieMongoDBRepository.save(movie);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "JOB WITH ID " + jobId + " REMOVED SUCCESSFULLY FROM CELEBRITY"));

        } catch (CelebrityNotFoundException | MovieNotFoundInMongoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "AN ERROR OCCURRED WHILE REMOVING THE JOB", "error", e.getMessage()));
        }
    }

    //METHOD FOR SEARCHING FOR A CELEBRITY'S JOBS
    @Transactional
    public ResponseEntity<Object> getJobsForCelebrity(int celebrityId) {
        try {
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(celebrityId)
                    .orElseThrow(() -> new IllegalArgumentException("CELEBRITY WITH ID " + celebrityId + " NOT FOUND!"));

            List<Job> jobs = celebrity.getJobs();

            if (jobs == null || jobs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "NO JOBS FOUND FOR CELEBRITY WITH ID " + celebrityId, "jobs", List.of()));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "JOBS RETRIEVED SUCCESSFULLY", "jobs", jobs));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred", "error", e.getMessage()));
        }
    }

    //METHOD TO UPDATE A CELEBRITY
    @Transactional
    public ResponseEntity<Object> updateCelebrity(String personId, String name, String poster) {
        try {
            int id = Integer.parseInt(personId);
            CelebrityMongoDB mongoCelebrity = celebrityMongoRepository.findById(id).orElse(null);

            if (mongoCelebrity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "CELEBRITY WITH ID " + personId + " NOT FOUND IN THE DATABASES"));
            }

            if (name != null && !name.trim().isEmpty()) {
                mongoCelebrity.setName(name);
            }

            if (poster != null && !poster.trim().isEmpty()) {
                mongoCelebrity.setPoster(poster);
            }

            celebrityMongoRepository.save(mongoCelebrity);

            Optional<CelebrityNeo4J> neo4jCelebrityOptional = celebrityNeo4JRepository.updateCelebrity(personId, name, poster);

            if (neo4jCelebrityOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "CELEBRITY UPDATED SUCCESSFULLY."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "FAILED TO UPDATED CELEBRUTY IN NEO4J."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while updating the celebrity", "error", e.getMessage()));
        }
    }

    //METHOD TO FIND NEO4J INCONSISTENCIES
    public List<String> celebrityInconsistenciesNeo() {
        List<String> mongoDbIds = celebrityMongoRepository.findAllIds().getAllIds();
        List<String> neo4jIds = celebrityNeo4JRepository.findAllIds();
        return findDifference(neo4jIds, mongoDbIds);
    }

    //METHOD TO FIND MONGO INCONSISTENCIES
    public List<String> celebrityInconsistenciesMongo() {
        List<String> mongoDbIds = celebrityMongoRepository.findAllIds().getAllIds();
        List<String> neo4jIds = celebrityNeo4JRepository.findAllIds();
        return findDifference(mongoDbIds, neo4jIds);
    }
}