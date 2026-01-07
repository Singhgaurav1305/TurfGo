package com.turf.management.service;

import com.turf.management.model.User;
import com.turf.management.repository.UserRepository;
import org.springframework.stereotype.Service; // Import Added
import org.springframework.beans.factory.annotation.Autowired; // Import Added
import java.util.List;

@Service // <-- YE SABSE ZARURI HAI
public class UserService {

    private final UserRepository userRepository;

    @Autowired // Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- LOGIN METHOD ---
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // --- REGISTER METHOD ---
    public boolean registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    // --- OTHER METHODS ---
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public boolean deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public boolean changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}