package com.example.demo.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.RendezVous;
import com.example.demo.Repositories.RendezVousRepository;

@Service
public class RendezVousService {
    
    // Status constants
    public static final String STATUT_EN_ATTENTE = "EN_ATTENTE";
    public static final String STATUT_CONFIRME = "CONFIRME";
    public static final String STATUT_ANNULE = "ANNULE";
    public static final String STATUT_TERMINE = "TERMINE";
    
    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    // Get all appointments
    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }
    
    // Get appointment by ID
    public Optional<RendezVous> getRendezVousById(Long id) {
        return rendezVousRepository.findById(id);
    }
    
    // Get today's appointments
    public List<RendezVous> getTodayRendezVous() {
        LocalDate today = LocalDate.now();
        return rendezVousRepository.findAll().stream()
                .filter(rv -> rv.getDate().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }
    
    // Get today's appointments count
    public long getTodayRendezVousCount() {
        LocalDate today = LocalDate.now();
        return rendezVousRepository.findAll().stream()
                .filter(rv -> rv.getDate().toLocalDate().equals(today))
                .count();
    }
    
    // Get appointments by patient ID
    public List<RendezVous> getRendezVousByPatient(Long patientId) {
        return rendezVousRepository.findAll().stream()
                .filter(rv -> rv.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }
    
    // Get appointments by doctor ID
    public List<RendezVous> getRendezVousByMedecin(Long medecinId) {
        return rendezVousRepository.findAll().stream()
                .filter(rv -> rv.getMedecin().getId().equals(medecinId))
                .collect(Collectors.toList());
    }
    
    // Create new appointment
    public RendezVous createRendezVous(RendezVous rendezVous) {
        // Set default status to EN_ATTENTE for new appointments
        if (rendezVous.getStatut() == null || rendezVous.getStatut().isEmpty()) {
            rendezVous.setStatut(STATUT_EN_ATTENTE);
        }
        return rendezVousRepository.save(rendezVous);
    }
    
    // Update appointment
    public Optional<RendezVous> updateRendezVous(Long id, RendezVous rendezVous) {
        try {
            Optional<RendezVous> existingRendezVousOpt = rendezVousRepository.findById(id);
            if (existingRendezVousOpt.isPresent()) {
                RendezVous existingRendezVous = existingRendezVousOpt.get();
                
                // Update fields from the request data
                if (rendezVous.getDate() != null) {
                    existingRendezVous.setDate(rendezVous.getDate());
                }
                
                if (rendezVous.getHeure() != null) {
                    existingRendezVous.setHeure(rendezVous.getHeure());
                }
                
                if (rendezVous.getDuree() != null) {
                    existingRendezVous.setDuree(rendezVous.getDuree());
                }
                
                if (rendezVous.getMotif() != null) {
                    existingRendezVous.setMotif(rendezVous.getMotif());
                }
                
                if (rendezVous.getStatut() != null) {
                    existingRendezVous.setStatut(rendezVous.getStatut());
                }
                
                // Handle patient reference
                if (rendezVous.getPatient() != null && rendezVous.getPatient().getId() != null) {
                    // Only update the patient if it's a valid reference
                    existingRendezVous.setPatient(rendezVous.getPatient());
                }
                
                // Handle medecin reference
                if (rendezVous.getMedecin() != null && rendezVous.getMedecin().getId() != null) {
                    // Only update the medecin if it's a valid reference
                    existingRendezVous.setMedecin(rendezVous.getMedecin());
                }
                
                return Optional.of(rendezVousRepository.save(existingRendezVous));
            }
            return Optional.empty();
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error updating rendez-vous: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    // Delete appointment
    public boolean deleteRendezVous(Long id) {
        if (rendezVousRepository.existsById(id)) {
            rendezVousRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Get upcoming appointments
    public List<RendezVous> getUpcomingRendezVous() {
        LocalDate today = LocalDate.now();
        return rendezVousRepository.findAll().stream()
                .filter(rv -> rv.getDate().toLocalDate().isAfter(today) || rv.getDate().toLocalDate().equals(today))
                .sorted((rv1, rv2) -> rv1.getDate().compareTo(rv2.getDate()))
                .limit(10) // Limit to 10 upcoming appointments
                .collect(Collectors.toList());
    }
    
    // Get appointments by status
    public List<RendezVous> getRendezVousByStatus(String status) {
        return rendezVousRepository.findAll().stream()
                .filter(rv -> rv.getStatut() != null && rv.getStatut().equals(status))
                .collect(Collectors.toList());
    }
    
    // Update appointment status
    public Optional<RendezVous> updateRendezVousStatus(Long id, String status) {
        Optional<RendezVous> rendezVousOpt = rendezVousRepository.findById(id);
        
        if (rendezVousOpt.isPresent()) {
            RendezVous rendezVous = rendezVousOpt.get();
            
            // Validate status
            if (status.equals(STATUT_EN_ATTENTE) || 
                status.equals(STATUT_CONFIRME) || 
                status.equals(STATUT_ANNULE) || 
                status.equals(STATUT_TERMINE)) {
                
                rendezVous.setStatut(status);
                return Optional.of(rendezVousRepository.save(rendezVous));
            }
        }
        return Optional.empty();
    }
}
