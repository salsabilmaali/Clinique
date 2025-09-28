package com.example.demo.Services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Medecin;
import com.example.demo.Models.Patient;
import com.example.demo.Models.Role;
import com.example.demo.Models.Specialite;
import com.example.demo.Models.Utilisateur;
import com.example.demo.Repositories.UtilisateurRepository;

@Service
public class UserService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    // Get all users
    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }
    
    // Get user count
    public long getUserCount() {
        return utilisateurRepository.count();
    }
    
    // Get recent users with limit
    public List<Utilisateur> getRecentUsers(Integer limit) {
        if (limit == null) {
            limit = 10;
        }
        return utilisateurRepository.findAll().stream()
                .sorted((u1, u2) -> u2.getId().compareTo(u1.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    // Get all doctors (medecins)
    public List<Medecin> getAllMedecins() {
        return utilisateurRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.MEDECIN)
                .map(u -> (Medecin) u)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all doctors by specialty
     * @param specialite The specialty to filter by
     * @return List of doctors with the specified specialty
     */
    public List<Medecin> getMedecinsBySpecialite(Specialite specialite) {
        return utilisateurRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.MEDECIN)
                .map(u -> (Medecin) u)
                .filter(m -> m.getSpecialite() != null && m.getSpecialite().equals(specialite))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all doctors by specialty name
     * @param specialiteName The name of the specialty to filter by
     * @return List of doctors with the specified specialty
     */
    public List<Medecin> getMedecinsBySpecialiteName(String specialiteName) {
        try {
            Specialite specialite = Specialite.valueOf(specialiteName);
            return getMedecinsBySpecialite(specialite);
        } catch (IllegalArgumentException e) {
            // If the specialty name is invalid, return an empty list
            return Collections.emptyList();
        }
    }
    
    // Get active doctors count
    public long getActiveMedecinsCount() {
        return utilisateurRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.MEDECIN)
                .count();
    }
    
    // Get all patients
    public List<Patient> getAllPatients() {
        return utilisateurRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.PATIENT)
                .map(u -> (Patient) u)
                .collect(Collectors.toList());
    }
    
    // Get new patients count within specified days
    public long getNewPatientsCount(Integer days) {
        if (days == null) {
            days = 7;
        }
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return utilisateurRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.PATIENT)
                .filter(u -> u.getDateInscription() != null && u.getDateInscription().isAfter(cutoffDate))
                .count();
    }
    
    // Get new patients within specified days
    public List<Patient> getNewPatients(Integer days) {
        if (days == null) {
            days = 7;
        }
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return utilisateurRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.PATIENT)
                .filter(u -> u.getDateInscription() != null && u.getDateInscription().isAfter(cutoffDate))
                .map(u -> (Patient) u)
                .collect(Collectors.toList());
    }
    
    // Delete user by ID
    public boolean deleteUser(Long id) {
        if (utilisateurRepository.existsById(id)) {
            utilisateurRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Get user by ID
    public Optional<Utilisateur> getUserById(Long id) {
        return utilisateurRepository.findById(id);
    }
    
    // Get user by email
    public Optional<Utilisateur> getUserByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    
    // Create or update user
    public Utilisateur saveUser(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }
}
