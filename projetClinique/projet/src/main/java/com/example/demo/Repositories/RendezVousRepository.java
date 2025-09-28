package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.RendezVous;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // Custom queries can be added here if needed
}
