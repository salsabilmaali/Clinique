package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.DossierMedical;
import com.example.demo.Models.Patient;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {
    DossierMedical findByPatient(Patient patient);
}
