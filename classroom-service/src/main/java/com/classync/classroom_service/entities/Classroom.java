package com.classync.classroom_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String className;


    @Column(nullable = false, unique = true, length = 10)
    private String classroomCode;

//    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<UserClassroom> userClassrooms = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PrePersist
    public void generateClassroomCode() {
        this.classroomCode = generateRandomCode(10);
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    private String generateRandomCode(int length) {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphanumeric.length());
            code.append(alphanumeric.charAt(index));
        }
        return code.toString();
    }
}