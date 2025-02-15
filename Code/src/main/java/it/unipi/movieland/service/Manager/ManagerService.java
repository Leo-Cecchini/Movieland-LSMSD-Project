package it.unipi.movieland.service.Manager;

import it.unipi.movieland.model.Manager.Manager;
import it.unipi.movieland.repository.Manager.ManagerRepository;
import it.unipi.movieland.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ManagerService {

    public static String encrypt(String str, String secretKey) throws Exception {
        return UserService.encrypt(str, secretKey);
    }

    @Autowired
    private ManagerRepository managerRepository;

    public List<Manager> getAllManagers(int page, int size) {
        return managerRepository.findAll(PageRequest.of(page,size)).getContent();
    }

    public Manager getManagerById(String id) {
        Optional<Manager> manager = managerRepository.findById(id);
        if (manager.isEmpty()) {throw new NoSuchElementException("Manager " + id + " not found");}
        return manager.get();
    }

    @Transactional
    public Manager addManager(String username, String email, String password) {
        if (managerRepository.existsById(username)) {
            throw new IllegalArgumentException("Manger '" + username + "' already exists");
        } else if (managerRepository.existsByEmail((email))){
            throw new IllegalArgumentException("Mail'" + email + "' already used");
        }
        try {
            String passwordE=encrypt(password,"MovieLand0123456");
            Manager manager = new Manager(username, email, passwordE);
            managerRepository.save(manager);
            return manager;
        } catch (Exception e) {
            managerRepository.deleteById(username);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteManager(String username) {
        if (!managerRepository.existsById(username)) {
            throw new NoSuchElementException("Manager '" + username + "' doesn't exists");
        }
        try {
            managerRepository.deleteById(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticate(String username, String password) {
        if (!managerRepository.existsById(username)) {
            throw new NoSuchElementException("Manager '" + username + "' doesn't exists");
        }
        try {
            String pass=managerRepository.findById(username).get().getPassword();
            String passwordE=encrypt(password,"MovieLand0123456");
            return pass.equals(passwordE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
