package com.taskmanager.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}