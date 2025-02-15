package it.unipi.movieland.service.Celebrity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import it.unipi.movieland.model.Movie.Movie;
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

    //METHOD TO RETRIEVE ALL CELEBRITIES (MONGODB)
    public Page<CelebrityMongoDto> getAllCelebritiesMongo(Pageable pageable) {

        Page<CelebrityMongoDB> list = celebrityMongoRepository.findAll(pageable);
        List<CelebrityMongoDto> dtoList = list.getContent().stream()
                .map(CelebrityMongoDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, list.getTotalElements());
    }

    //METHOD TO RETRIEVE ALL CELEBRITIES (NEO4J)
    public Page<CelebrityNeo4JDto> getAllCelebritiesNeo4J(PageRequest pageRequest) {

        Page<CelebrityNeo4J> celebritiesPage = celebrityNeo4JRepository.findAll(pageRequest);
        List<CelebrityNeo4JDto> dtoList = celebritiesPage.getContent().stream()
                .map(CelebrityNeo4JDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageRequest, celebritiesPage.getTotalElements());
    }

    //METHOD TO RETRIEVE A CELEBRITY USING THEIR ID (MONGODB)
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

    // METHOD TO SEARCH FOR AN ACTOR BY NAME OR CHARACTER (MONGODB)
    public List<CelebrityMongoDto> searchActorsByCharacterMongo(String searchTerm) {
        List<CelebrityMongoDB> celebrities = celebrityMongoRepository.searchActorsAndCharacters(searchTerm);

        if (celebrities.isEmpty()) {
            throw new RuntimeException("NO CELEBRITIES FOUND FOR THE SEARCH TERM: " + searchTerm);
        }

        return celebrities.stream()
                .map(CelebrityMongoDto::fromEntity)
                .collect(Collectors.toList());
    }

    //METHOD FOR CREATING A CELEBRITY (MONGODB)
    public CelebrityMongoDB createCelebrityMongo(CelebrityMongoDB celebrity) {
        return celebrityMongoRepository.save(celebrity);
    }

    //METHOD FOR CREATING A CELEBRITY (NEO4J)
    public void createCelebrityNeo4j(CelebrityNeo4J celebrity) {
        celebrityNeo4JRepository.save(celebrity);
    }

    // METHOD TO CREATE A CELEBRITY IN BOTH DATABASES
    public String createCelebrityInBothDatabases(int id, String name, String poster) {
        try {

            CelebrityMongoDto existingMongoCelebrity = null;
            CelebrityNeo4JDto existingNeo4jCelebrity = null;

            try {
                existingMongoCelebrity = getCelebrityByIdMongo(id);
            } catch (CelebrityNotFoundException e) {

            }

            try {
                existingNeo4jCelebrity = getCelebrityByIdNeo4j(String.valueOf(id));
            } catch (CelebrityNotFoundException e) {

            }

            if (existingMongoCelebrity != null && existingNeo4jCelebrity != null) {
                return "CELEBRITY WITH ID " + id + " ALREADY EXISTS IN BOTH DATABASES";
            }

            CelebrityMongoDB newMongoCelebrity = new CelebrityMongoDB(id, name, poster);
            CelebrityMongoDB savedMongoCelebrity = createCelebrityMongo(newMongoCelebrity);

            CelebrityNeo4J newNeo4jCelebrity = new CelebrityNeo4J(String.valueOf(savedMongoCelebrity.getId()), name, poster);
            createCelebrityNeo4j(newNeo4jCelebrity);

            return "CELEBRITY CREATED SUCCESSFULLY IN BOTH DATABASES";

        } catch (Exception e) {
            throw new RuntimeException("ERROR CREATING CELEBRITY IN BOTH DATABASES: " + e.getMessage());
        }
    }

    //METHOD TO DELETE A CELEBRITY (MONGO DB)
    public void deleteCelebrityMongo(int id) {
        Optional<CelebrityMongoDB> celebrity = celebrityMongoRepository.findById(id);

        if (celebrity.isEmpty()) {
            throw new CelebrityNotFoundInMongoException("CELEBRITY WITH ID " + id + " NOT FOUND IN THE DATABASE");
        }
        celebrityMongoRepository.deleteById(id);
    }

    //METHOD TO DELETE A CELEBRITY (NEO4J)
    public void deleteCelebrityNeo4j(int id) {
        Optional<CelebrityNeo4J> celebrity = celebrityNeo4JRepository.findById(String.valueOf(id));

        if (celebrity.isEmpty()) {
            throw new CelebrityNotFoundInNeo4jException("CELEBRITY WITH ID " + id + " NOT FOUND IN THE DATABASE");
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
                    String[] parts = job.getJob_id().split("_job");
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
    public ResponseEntity<Object> addJobToActor(int id, String movie_id, String character) {
        try {
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(id)
                    .orElseThrow(() -> new CelebrityNotFoundException("CELEBRITY WITH ID " + id + " NOT FOUND"));

            Movie movie = movieMongoDBRepository.findById(movie_id)
                    .orElseThrow(() -> new MovieNotFoundException("MOVIE WITH ID " + movie_id + " NOT FOUND"));

            if (celebrity.getJobs() == null) {
                celebrity.setJobs(new ArrayList<>());
            }

            boolean jobExists = celebrity.getJobs().stream()
                    .anyMatch(job -> movie_id.equals(job.getMovie_id()) && character != null && character.equals(job.getCharacter()));

            if (jobExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "JOB ALREADY EXISTS FOR MOVIE ID: " + movie_id + " AND CHARACTER: " + character));
            }

            String jobId = generateJobIdForCelebrity(celebrity);
            Job newJob = new Job(celebrity, "Actor", movie_id, movie.getTitle(), character);
            newJob.setJob_id(jobId);

            celebrity.getJobs().add(newJob);
            celebrityMongoRepository.save(celebrity);

            JobDto jobDto = JobDto.fromEntity(newJob);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "JOB ADDED SUCCESSFULLY FOR ACTOR WITH ID " + id, "job", jobDto));

        } catch (CelebrityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (MovieNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
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

            Movie movie = movieMongoDBRepository.findById(movie_id)
                    .orElseThrow(() -> new IllegalArgumentException("MOVIE WITH ID " + movie_id + " NOT FOUND"));

            if (celebrity.getJobs() == null) {
                celebrity.setJobs(new ArrayList<>());
            }

            boolean jobAlreadyExists = celebrity.getJobs().stream()
                    .anyMatch(job -> job.getMovie_id().equals(movie_id) && job.getRole().equalsIgnoreCase("Director"));

            if (jobAlreadyExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "JOB ALREADY EXISTS FOR MOVIE ID: " + movie_id + " AND ROLE: DIRECTOR."));
            }

            String jobId = generateJobIdForCelebrity(celebrity);
            Job newJob = new Job();
            newJob.setJob_id(jobId);
            newJob.setRole("Director");
            newJob.setMovie_id(movie_id);
            newJob.setMovie_title(movie.getTitle());

            celebrity.getJobs().add(newJob);
            celebrityMongoRepository.save(celebrity);

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

    //REMOVE JOBS TO ACTOR O DIRECTOR
    @Transactional
    public ResponseEntity<Object> removeJobById(int celebrityId, String jobId) {
        try {
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(celebrityId)
                    .orElseThrow(() -> new CelebrityNotFoundException("CELEBRITY WITH ID " + celebrityId + " NOT FOUND!"));

            List<Job> jobs = celebrity.getJobs();
            if (jobs == null || jobs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "NO JOBS FOUND FOR CELEBRITY WITH ID " + celebrityId));
            }

            boolean jobFound = jobs.removeIf(job -> job.getJob_id() != null && job.getJob_id().equals(jobId));

            if (jobFound) {
                celebrityMongoRepository.save(celebrity);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "JOB WITH ID " + jobId + " REMOVED SUCCESSFULLLY FROM CELEBRITY WITH ID " + celebrityId));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "JOB WITH ID " + jobId + " NOT FOUND FOR CELEBRITY WITH ID " + celebrityId));
            }

        } catch (CelebrityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
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

    //METHOD TO UPDATE A CELEBRITY (MONGODB - NEO4J)
    @Transactional
    public ResponseEntity<Object> updateCelebrity(String personId, String name, String poster) {
        try {
            int id = Integer.parseInt(personId); // Assumiamo che personId sia un numero
            CelebrityMongoDB mongoCelebrity = celebrityMongoRepository.findById(id).orElse(null);

            if (mongoCelebrity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Celebrity with personId " + personId + " not found in MongoDB"));
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
                        .body(Map.of("message", "Celebrity updated successfully in both MongoDB and Neo4j"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Failed to update celebrity in Neo4j"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while updating the celebrity", "error", e.getMessage()));
        }
    }

    // METHOD TO FIND NEO4J INCONSISTENCIES
    public List<String> celebrityInconsistenciesNeo() {
        List<String> mongoDbIds = celebrityMongoRepository.findAllIds().getAllIds();
        List<String> neo4jIds = celebrityNeo4JRepository.findAllIds();
        return findDifference(neo4jIds, mongoDbIds);
    }

    // METHOD TO FIND MONGO INCONSISTENCIES
    public List<String> celebrityInconsistenciesMongo() {
        List<String> mongoDbIds = celebrityMongoRepository.findAllIds().getAllIds();
        List<String> neo4jIds = celebrityNeo4JRepository.findAllIds();
        return findDifference(mongoDbIds, neo4jIds);
    }
}