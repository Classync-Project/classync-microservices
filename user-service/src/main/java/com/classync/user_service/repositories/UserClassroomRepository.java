package com.classync.user_service.repositories;

import com.classync.user_service.entities.User;
import com.classync.user_service.entities.UserClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserClassroomRepository extends JpaRepository<UserClassroom, Long> {
    Optional<UserClassroom> findByUserIdAndClassroomId(int userId, Long classroomId);

    boolean existsByUserIdAndClassroomId(int userId, Long classroomId);

//    List<Classroom> findClassroomsByUser(User user);
}