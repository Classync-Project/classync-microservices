package com.classync.classroom_service.repositories;

import com.classync.classroom_service.entities.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Classroom findByClassName(String className);

    boolean existsByClassroomCode(String classroomCode);

    boolean existsByClassName(String className);
    Optional<Classroom> findByClassroomCode(String classroomCode);
}
