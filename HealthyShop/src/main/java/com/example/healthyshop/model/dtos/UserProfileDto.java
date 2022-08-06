package com.example.healthyshop.model.dtos;

import com.example.healthyshop.model.enums.RoleName;

import java.util.Set;

public class UserProfileDto {
    private long id;
    private String username;
    private String email;
    private String fullName;
    private int age;
    private Set<RoleName> roles;

    public UserProfileDto() {
    }

    public UserProfileDto(long id, String username, String email, String fullName, int age, Set<RoleName> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.age = age;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<RoleName> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleName> roles) {
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
