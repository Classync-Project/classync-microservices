package com.classync.user_service.services;

import com.classync.user_service.entities.User;
import com.classync.user_service.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email);
    }

    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}