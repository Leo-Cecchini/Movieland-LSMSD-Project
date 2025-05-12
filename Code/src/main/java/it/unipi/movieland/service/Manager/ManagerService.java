package it.unipi.movieland.service.Manager;

import it.unipi.movieland.model.Manager.Manager;
import it.unipi.movieland.repository.Manager.ManagerRepository;
import it.unipi.movieland.service.User.UserService;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ManagerService {

    private static final String ENCRYPTION_KEY = "MovieLand0123456";

    @Autowired
    private ManagerRepository managerRepository;

    //METHOD TO ENCRYPT STRING
    public static String encrypt(String str, String secretKey) throws Exception {
        return UserService.encrypt(str, secretKey);
    }

    //METHOD TO GET ALL MANAGERS WITH PAGINATION
    public List<Manager> getAllManagers(int page, int size) {
        return managerRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    //METHOD TO GET MANAGER BY ID
    public Manager getManagerById(String id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("MANAGER WITH ID '" + id + "' NOT FOUND."));
    }

    //METHOD TO ADD A NEW MANAGER
    @Transactional
    public Manager addManager(String username, String email, String password) {

        if (username == null || username.length() < 5) {
            throw new IllegalArgumentException("USERNAME MUST BE AT LEAST 5 CHARACTERS LONG.");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email == null || !Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("INVALID EMAIL FORMAT.");
        }

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if (password == null || !Pattern.matches(passwordRegex, password)) {
            throw new IllegalArgumentException("PASSWORD MUST BE AT LEAST 8 CHARACTERS LONG AND INCLUDE A CAPITAL LETTER, A LOWERCASE LETTER, A NUMBER, AND A SPECIAL CHARACTER.");
        }

        if (managerRepository.existsById(username)) {
            throw new IllegalArgumentException("MANAGER WITH USERNAME '" + username + "' ALREADY EXISTS.");
        }

        if (managerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("THE EMAIL '" + email + "' IS ALREADY IN USE.");
        }

        try {
            String encryptedPassword = encrypt(password, ENCRYPTION_KEY);
            Manager manager = new Manager(username, email, encryptedPassword);
            return managerRepository.save(manager);
        } catch (Exception e) {
            throw new RuntimeException("ERROR WHILE ENCRYPTING THE PASSWORD.", e);
        }
    }

    /*
    @Transactional
    public Manager addManager(String username, String email, String password) {
        if (managerRepository.existsById(username)) {
            throw new IllegalArgumentException("MANAGER WITH USERNAME '" + username + "' ALREADY EXISTS.");
        } else if (managerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("THE EMAIL '" + email + "' IS ALREADY IN USE.");
        }

        try {
            String encryptedPassword = encrypt(password, ENCRYPTION_KEY);
            Manager manager = new Manager(username, email, encryptedPassword);
            return managerRepository.save(manager);
        } catch (Exception e) {
            throw new RuntimeException("ERROR WHILE ENCRYPTING THE PASSWORD.", e);
        }
    }
     */

    //METHOD TO DELETE A MANAGER BY USERNAME
    @Transactional
    public void deleteManager(String username) {
        if (!managerRepository.existsById(username)) {
            throw new NoSuchElementException("MANAGER WITH USERNAME '" + username + "' DOES NOT EXIST.");
        }
        managerRepository.deleteById(username);
    }

    //METHOD TO AUTHENTICATE A MANAGER
    public boolean authenticate(String username, String password) {
        Manager manager = managerRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("MANAGER WITH USERNAME '" + username + "' DOES NOT EXIST."));

        try {
            String encryptedPassword = encrypt(password, ENCRYPTION_KEY);
            System.out.println(encryptedPassword);
            return manager.getPassword().equals(encryptedPassword);
        } catch (Exception e) {
            throw new RuntimeException("ERROR WHILE ENCRYPTING THE PASSWORD.", e);
        }
    }
}