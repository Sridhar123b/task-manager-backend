package com.taskmanager.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.backend.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}