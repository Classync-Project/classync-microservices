package com.classync.user_service.controllers;
import com.classync.user_service.dto.UserDto;
import com.classync.user_service.entities.User;
import com.classync.user_service.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final RestTemplate restTemplate;

    @Value("${classroom.service.url}")
    private String classroomServiceUrl;

    public UserController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        try {
            User user = userService.findById(updatedUser.getId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            user.setFullName(updatedUser.getFullName());
            user.setPicture(updatedUser.getPicture());
            userService.saveUser(user);

            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAuthenticatedUser(@AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        Optional<User> userOpt = userService.findUserByEmail(oidcUser.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();

        // Fetch classroom details from Classroom Service
        String classroomUrl = classroomServiceUrl + "/user/" + user.getId();
        ResponseEntity<List> classroomResponse = restTemplate.getForEntity(classroomUrl, List.class);
        List<?> classrooms = classroomResponse.getBody();

        return ResponseEntity.ok(new UserDetails(
                user.getId(),
                oidcUser.getEmail(),
                oidcUser.getFullName(),
                oidcUser.getPicture(),
                classrooms  // Includes fetched classrooms
        ));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public record UserDetails(int id, String email, String fullName, String picture, List<?> classrooms) {
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.findAllUsers();
            List<UserDto> userDtos = users.stream()
                    .map(user -> new UserDto(user.getId(), user.getFullName(), user.getEmail()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch users: " + e.getMessage());
        }
    }

}

