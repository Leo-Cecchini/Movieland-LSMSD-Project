package it.unipi.movieland.service.Celebrity;

import it.unipi.movieland.model.Celebrity.CelebrityMongoDB;
import it.unipi.movieland.model.Celebrity.CelebrityNeo4J;
import it.unipi.movieland.repository.Celebrity.CelebrityMongoDBRepository;
import it.unipi.movieland.repository.Celebrity.CelebrityNeo4JRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import it.unipi.movieland.model.Celebrity.Job;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

//
@Service
public class CelebrityService {

    private final CelebrityMongoDBRepository celebrityMongoRepository;
    private final CelebrityNeo4JRepository celebrityNeo4JRepository;

    @Autowired
    public CelebrityService(CelebrityMongoDBRepository mongoRepository, CelebrityNeo4JRepository neo4jRepository) {
        this.celebrityMongoRepository = mongoRepository;
        this.celebrityNeo4JRepository = neo4jRepository;
    }

    @Transactional
    public void createCelebrity(CelebrityMongoDB mongoCelebrity, CelebrityNeo4J neo4jCelebrity) {
        celebrityMongoRepository.save(mongoCelebrity);
        celebrityNeo4JRepository.save(neo4jCelebrity);
    }

    //METODO PER CERCARE TUTTE LE CELEBRITA' (MONGODB)
    public Page<CelebrityMongoDB> getAllCelebritiesMongo(Pageable pageable) {
        return celebrityMongoRepository.findAll(pageable);
    }

    //METODO PER CREARE UNA CELEBRITA' (MONGODB)
    public CelebrityMongoDB createCelebrityMongo(CelebrityMongoDB celebrity) {
        return celebrityMongoRepository.save(celebrity);
    }

    //METODO PER CREARE UNA CELEBRITA' (NEO4J)
    public void createCelebrityNeo4j(CelebrityNeo4J celebrity) {
        celebrityNeo4JRepository.save(celebrity);
    }

    //METODO PER RECUPERARE UNA CELEBRITA' TRAMITE ID (MONGODB)
    public Optional<CelebrityMongoDB> getCelebrityByIdMongo(int id) {
        return celebrityMongoRepository.findById(id);
    }

    //METODO PER RECUPERARE UNA CELEBRITA' TRAMITE ID (NEO4J)
    public Optional<CelebrityNeo4J> getCelebrityByIdNeo4j(String personId) {
        return celebrityNeo4JRepository.findById(personId);
    }

    //METODO PER ELIMINARE UNA CELEBRITA' (MONGO DB)
    public void deleteCelebrityMongo(int id) {
        // Verifica se esiste
        Optional<CelebrityMongoDB> celebrity = celebrityMongoRepository.findById(id);
        if (celebrity.isEmpty()) {
            throw new RuntimeException("Celebrity with ID " + id + " not found in MongoDB");
        }
        // Elimina dal MongoDB
        celebrityMongoRepository.deleteById(id);
    }

    //METODO PER ELIMINARE UNA CELEBRITA' (NEO4J)
    public void deleteCelebrityNeo4j(int id) {
        // Verifica se esiste
        Optional<CelebrityNeo4J> celebrity = celebrityNeo4JRepository.findById(String.valueOf(id));
        if (celebrity.isEmpty()) {
            throw new RuntimeException("Celebrity with ID " + id + " not found in Neo4j");
        }
        // Elimina dal Neo4j
        celebrityNeo4JRepository.deleteById(String.valueOf(id));
    }

    //METODO PER AGGIUNGERE UN JOB ALLA CELEBRITA' (MONGODB)
    @Transactional
    public boolean addJobToCelebrity(int id, Job newJob) {
        try {
            // Recupera la celebrity dalla repository
            CelebrityMongoDB celebrity = celebrityMongoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Celebrity with ID " + id + " not found"));

            // Inizializza la lista di jobs se è null
            if (celebrity.getJobs() == null) {
                celebrity.setJobs(new ArrayList<>());
            }

            // Aggiungi il nuovo job alla lista
            celebrity.getJobs().add(newJob);

            // Salva l'aggiornamento nel database
            celebrityMongoRepository.save(celebrity);

            return true; // Operazione riuscita
        } catch (Exception e) {
            e.printStackTrace();  // Stampa lo stack trace per vedere l'errore preciso
            return false; // In caso di errore
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
                    boolean jobFound = jobs.removeIf(job -> job.getJob_id().equals(jobId));

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

    //METODO PER RECUPERARE TUTTE LE CELEBRITA' CON PAGINAZIONE(NEO4J)
    public Page<CelebrityNeo4J> getAllCelebrities(int page, int size) {
        return celebrityNeo4JRepository.findAll(PageRequest.of(page, size));
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

    //Metodo per raccomandare attori in base ai film apprezzati dall'utente (NEO4J)
    public List<Map<String, Object>> recommendActorsByUserLikes(String username) {
        return celebrityNeo4JRepository.recommendActorsByUserLikes(username);
    }

    //Metodi per raccomandare attori in base agli attori seguiti dall'utente (NEO4J)
    public List<Map<String, Object>> recommendActorsByFollowedActors(String username) {
        return celebrityNeo4JRepository.recommendActorsByFollowedActors(username);
    }

    //Metodi per ottenere raccomandazioni di celebrity da connessioni di secondo grado (NEO4J)
    public List<Map<String, Object>> getSecondDegreeCelebrityRecommendations(String username) {
        return celebrityNeo4JRepository.recommendSecondDegreeCelebrities(username);
    }
}