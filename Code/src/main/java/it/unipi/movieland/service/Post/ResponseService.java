package applicationMovieland.service.Post;

import applicationMovieland.model.Post.Response;
import applicationMovieland.repository.Post.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    // Aggiungi una nuova risposta
    public Response addResponse(Response response) {
        return responseRepository.save(response);
    }

    // Ottieni tutte le risposte
    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }

    // Ottieni una risposta per id
    public Optional<Response> getResponseById(String id) {
        return responseRepository.findById(id);
    }

    // Elimina una risposta
    public void deleteResponse(String id) {
        responseRepository.deleteById(id);
    }
}