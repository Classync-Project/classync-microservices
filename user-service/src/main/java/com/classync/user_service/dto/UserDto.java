package com.classync.user_service.dto;

public class UserDto {
    private String id;
    private String name;
    private String email;

    public UserDto(int id, String name, String email) {
        this.id = String.valueOf(id);
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
