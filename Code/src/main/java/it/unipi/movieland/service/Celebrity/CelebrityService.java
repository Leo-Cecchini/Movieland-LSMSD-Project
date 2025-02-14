package it.unipi.movieland.service.Celebrity;

import it.unipi.movieland.model.Celebrity.CelebrityMongoDB;
import it.unipi.movieland.model.Celebrity.CelebrityNeo4J;
import it.unipi.movieland.model.Movie.Movie;
import it.unipi.movieland.repository.Celebrity.CelebrityMongoDBRepository;
import it.unipi.movieland.repository.Celebrity.CelebrityNeo4JRepository;
import it.unipi.movieland.repository.Movie.MovieMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;
import it.unipi.movieland.model.Celebrity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
//


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

    //METODO PER CREARE UNA CELEBRITA' (MONGODB)
    public CelebrityMongoDB createCelebrityMongo(CelebrityMongoDB celebrity) {
        return celebrityMongoRepository.save(celebrity);
    }

    //METODO PER CREARE UNA CELEBRITA' (NEO4J)
    public void createCelebrityNeo4j(CelebrityNeo4J celebrity) {
        celebrityNeo4JRepository.save(celebrity);
    }

    //METODO PER CERCARE TUTTE LE CELEBRITA' (MONGODB)
    public Page<CelebrityMongoDB> getAllCelebritiesMongo(Pageable pageable) {
        return celebrityMongoRepository.findAll(pageable);
    }

    //METODO PER RECUPERARE TUTTE LE CELEBRITA' CON PAGINAZIONE(NEO4J)
    public Page<CelebrityNeo4J> getAllCelebrities(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number and size must be positive values.");
        }
        return celebrityNeo4JRepository.findAll(PageRequest.of(page, size));
    }

    //METODO PER RECUPERARE UNA CELEBRITA' TRAMITE ID (MONGODB)
    public Optional<CelebrityMongoDB> getCelebrityByIdMongo(int id) {
        return celebrityMongoRepository.findById(id);
    }

    //METODO PER RECUPERARE UNA CELEBRITA' TRAMITE ID (NEO4J)
    public Optional<CelebrityNeo4J> getCelebrityByIdNeo4j(String personId) {
        return celebrityNeo4JRepository.findById(personId);
    }

    //METODO PER CREARE UNA CELEBRITA' IN ENTRAMBI I DATABASE
    public String createCelebrityInBothDatabases(int id, String name, String poster) {
        try {
            // Controlla se la celebrity esiste già in MongoDB e Neo4j
            Optional<CelebrityMongoDB> existingMongoCelebrity = getCelebrityByIdMongo(id);
            Optional<CelebrityNeo4J> existingNeo4jCelebrity = getCelebrityByIdNeo4j(String.valueOf(id));

            // Se esiste in entrambi i database, restituisci un errore
            if (existingMongoCelebrity.isPresent() && existingNeo4jCelebrity.isPresent()) {
                return "Celebrity with ID " + id + " already exists in both databases";
            }

            CelebrityMongoDB newMongoCelebrity = new CelebrityMongoDB(id, name, poster);
            CelebrityMongoDB savedMongoCelebrity = createCelebrityMongo(newMongoCelebrity);

            CelebrityNeo4J newNeo4jCelebrity = new CelebrityNeo4J(String.valueOf(savedMongoCelebrity.getId()),name,poster);
            createCelebrityNeo4j(newNeo4jCelebrity);

            return "Celebrity created successfully in both databases";

        } catch (Exception e) {
            throw new RuntimeException("Error creating celebrity in both databases: " + e.getMessage());
        }
    }

    //METODO PER ELIMINARE UNA CELEBRITA' (MONGO DB)
    public void deleteCelebrityMongo(int id) {
        Optional<CelebrityMongoDB> celebrity = celebrityMongoRepository.findById(id);
        if (celebrity.isEmpty()) {
            throw new RuntimeException("Celebrity with ID " + id + " not found in MongoDB");
        }
        celebrityMongoRepository.deleteById(id);
    }

    //METODO PER ELIMINARE UNA CELEBRITA' (NEO4J)
    public void deleteCelebrityNeo4j(int id) {
        Optional<CelebrityNeo4J> celebrity = celebrityNeo4JRepository.findById(String.valueOf(id));
        if (celebrity.isEmpty()) {
            throw new RuntimeException("Celebrity with ID " + id + " not found in Neo4j");
        }
        celebrityNeo4JRepository.deleteById(String.valueOf(id));
    }

    //METODO PER CREARE ID PER IL JOB
    private String generateJobIdForCelebrity(CelebrityMongoDB celebrity) {
        // Conta quanti job ci sono già per questa celebrity
        int jobCount = (celebrity.getJobs() != null) ? celebrity.getJobs().size() : 0;

        // Crea il nuovo job_id seguendo la logica "celebrityId_jobCount"
        return celebrity.getId() + "_job" + (jobCount + 1); // Incrementa il contatore per il nuovo job
    }

    //METODO PER AGGIUNGERE UN JOB AD UN ATTORE (MONGODB)
    @Transactional
    public boolean addJobToActor(int id, String movie_id, String movie_title, String character) {
        try {
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Celebrity with ID " + id + " not found"));

            Movie movie = movieMongoDBRepository.findById(movie_id)
                    .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + movie_id + " not found"));

            if (!movie.getTitle().equalsIgnoreCase(movie_title)) {
                return false;
            }

            if (celebrity.getJobs() == null) {
                celebrity.setJobs(new ArrayList<>());
            }

            String jobId = generateJobIdForCelebrity(celebrity);

            Job newJob = new Job();
            newJob.setJob_id(jobId);  // Imposta il job_id generato
            newJob.setRole("Actor");
            newJob.setMovie_id(movie_id);

            newJob.setMovie_title(movie.getTitle());
            newJob.setCharacter(character);

            boolean jobExists = celebrity.getJobs().stream()
                    .anyMatch(job -> job.getMovie_id().equals(movie_id) &&
                            job.getMovie_title().equalsIgnoreCase(movie.getTitle()) &&
                            job.getCharacter().equals(character));

            if (jobExists) {
                return false;
            }

            celebrity.getJobs().add(newJob);
            celebrityMongoRepository.save(celebrity);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //METODO PER AGGIUNGERE UN JOB AD UN DIRECTOR (MONGODB)
    @Transactional
    public boolean addJobToDirector(int id, String movie_id, String movie_title) {
        try {
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Celebrity with ID " + id + " not found"));

            System.out.println("Celebrity found: " + celebrity);

            Movie movie = movieMongoDBRepository.findById(movie_id)
                    .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + movie_id + " not found"));

            System.out.println("Movie found: " + movie);

            if (!movie.getTitle().equalsIgnoreCase(movie_title)) {
                System.out.println("Movie title mismatch: expected " + movie_title + ", found " + movie.getTitle());
                return false;
            }

            if (celebrity.getJobs() == null) {
                celebrity.setJobs(new ArrayList<>());
            }

            // Controllo se il job esiste già
            boolean jobAlreadyExists = celebrity.getJobs().stream()
                    .anyMatch(job -> job.getMovie_id().equals(movie_id) && job.getRole().equalsIgnoreCase("Director"));

            if (jobAlreadyExists) {
                System.out.println("Job already exists for movie_id: " + movie_id + " and role: Director");
                return false; // Evita duplicazione
            }

            String jobId = generateJobIdForCelebrity(celebrity);
            System.out.println("Generated job ID: " + jobId);

            Job newJob = new Job();
            newJob.setJob_id(jobId);
            newJob.setRole("Director");
            newJob.setMovie_id(movie_id);
            newJob.setMovie_title(movie.getTitle());

            celebrity.getJobs().add(newJob);
            celebrityMongoRepository.save(celebrity);

            System.out.println("Job added successfully to celebrity with ID: " + id);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e.getMessage());
            return false;
        }
    }

    //METODO PER RIMUOVERE UN JOB ALLA CELEBRITA' (MONGODB)
    @Transactional
    public boolean removeJobById(String jobId) {
        try {
            List<CelebrityMongoDB> allCelebrities = celebrityMongoRepository.findAll();

            for (CelebrityMongoDB celebrity : allCelebrities) {
                List<Job> jobs = celebrity.getJobs();
                if (jobs != null) {
                    // Rimuovi il job solo se il job_id è non-null e uguale a jobId
                    boolean jobFound = jobs.removeIf(job -> job.getJob_id() != null && job.getJob_id().equals(jobId));

                    if (jobFound) {
                        celebrityMongoRepository.save(celebrity);
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // In caso di errore
        }
    }
    //METODO PER CERCARE I JOB DI UNA CELEBRITA'
    @Transactional
    public List<Job> getJobsForCelebrity(int celebrityId) {
        CelebrityMongoDB celebrity = celebrityMongoRepository.findById(celebrityId)
                .orElseThrow(() -> new IllegalArgumentException("Celebrity with ID " + celebrityId + " not found"));

        return celebrity.getJobs(); // Ritorna la lista di job
    }

    //METODO PER AGGIORNARE UNA CELEBRITA' (MONGODB - NEO4J)
    @Transactional
    public boolean updateCelebrity(String personId, String name, String poster) {
        int id = Integer.parseInt(personId); // Assumiamo che personId sia un numero
        CelebrityMongoDB mongoCelebrity = celebrityMongoRepository.findById(id).orElse(null);
        if (mongoCelebrity == null) {
            return false;  // Non è stato trovato un record da aggiornare in MongoDB
        }
        mongoCelebrity.setName(name);
        mongoCelebrity.setPoster(poster);
        celebrityMongoRepository.save(mongoCelebrity); // Usa save() per aggiornare il documento in MongoDB

        Optional<CelebrityNeo4J> neo4jCelebrityOptional = celebrityNeo4JRepository.updateCelebrity(personId, name, poster);
        return neo4jCelebrityOptional.isPresent();  // Restituisce true se aggiornato anche in Neo4j
    }

    //METODO PER CERCARE UN ATTORE TRAMITE NOME O PAROLA CHIAVE (MONGODB)
    public List<CelebrityMongoDB> searchActorsByCharacterMongo(String searchTerm) {
        return celebrityMongoRepository.searchActorsAndCharacters(searchTerm);
    }

    //Metodi per ottenere raccomandazioni di celebrity da connessioni di secondo grado (NEO4J)
    public List<Map<String, Object>> getSecondDegreeCelebrityRecommendations(String username) {
        return celebrityNeo4JRepository.recommendSecondDegreeCelebrities(username);
    }

    public static List<String> findDifference(List<String> a, List<String> b) {
        Set<String> firstSet = new HashSet<>(a);

        List<String> result = new ArrayList<>();

        for (String mongoElement : b) {
            if (!firstSet.contains(mongoElement)) {
                result.add(mongoElement);
            }
        }

        return result;
    }

    public List<String> inconsistenciesNeo() {
        List<String> mongoDb=celebrityMongoRepository.findAllIds().getAllIds();
        List<String> neo4j=celebrityNeo4JRepository.findAllIds();
        return findDifference(neo4j, mongoDb);
    }
    public List<String> inconsistenciesMongo() {
        List<String> mongoDb=celebrityMongoRepository.findAllIds().getAllIds();
        List<String> neo4j=celebrityNeo4JRepository.findAllIds();
        return findDifference(mongoDb, neo4j);
    }
}