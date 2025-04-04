package com.classync.classroom_service.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.classync.classroom_service.repositories.ClassroomRepository;

@Service
@Transactional
public class ClassroomService {


    public final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }
}
