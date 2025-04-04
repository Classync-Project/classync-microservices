package com.classync.user_service.services;

import com.classync.user_service.entities.Role;
import com.classync.user_service.entities.User;
import com.classync.user_service.entities.UserClassroom;
import com.classync.user_service.repositories.RoleRepository;
import com.classync.user_service.repositories.UserClassroomRepository;
import com.classync.user_service.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserClassroomService {

    private final UserClassroomRepository userClassroomRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;


    public boolean isUserInClassroom(int userId, Long classroomId) {
        return userClassroomRepository.existsByUserIdAndClassroomId(userId, classroomId);
    }

    public UserClassroomService(UserClassroomRepository userClassroomRepository, RoleRepository roleRepository , UserRepository userRepository) {
        this.userClassroomRepository = userClassroomRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean updateUserRole(int userId, Long classroomId, String newRoleName) {

        UserClassroom userClassroom = userClassroomRepository.findByUserIdAndClassroomId(userId, classroomId)
                .orElseThrow(() -> new RuntimeException("UserClassroom not found"));
        System.out.println(userClassroom);
        Role newRole = roleRepository.findByName(newRoleName);

        userClassroom.setRole(newRole);

        userClassroomRepository.save(userClassroom);
        return true;
    }

    @Transactional
    public boolean unenrollStudent(String useremail, Long classroomId) {

        Optional<User> user = userRepository.findByEmail(useremail);

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        // Find the user-classroom relationship
        Optional<UserClassroom> userClassroomOpt = userClassroomRepository
                .findByUserIdAndClassroomId(user.get().getId(), classroomId);



        if (userClassroomOpt.isEmpty()) {
            throw new RuntimeException("User is not enrolled in this classroom");
        }

        UserClassroom userClassroom = userClassroomOpt.get();
        userClassroomRepository.delete(userClassroom);
        return true;
    }

}
