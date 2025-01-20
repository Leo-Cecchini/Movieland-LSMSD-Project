package applicationMovieland.controller.Post;

import applicationMovieland.model.Post.Response;
import applicationMovieland.service.Post.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/responses")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    // Endpoint per ottenere tutte le risposte
    @GetMapping
    public List<Response> getAllResponses() {
        return responseService.getAllResponses();
    }

    // Endpoint per ottenere una risposta per id
    @GetMapping("/{id}")
    public ResponseEntity<Response> getResponseById(@PathVariable String id) {
        Optional<Response> response = responseService.getResponseById(id);
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere una nuova risposta
    @PostMapping
    public ResponseEntity<Response> addResponse(@RequestBody Response response) {
        Response addedResponse = responseService.addResponse(response);
        return ResponseEntity.ok(addedResponse);
    }

    // Endpoint per eliminare una risposta tramite id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable String id) {
        responseService.deleteResponse(id);
        return ResponseEntity.noContent().build();
    }
}