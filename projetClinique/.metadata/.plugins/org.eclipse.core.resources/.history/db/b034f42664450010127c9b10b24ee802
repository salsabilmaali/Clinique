package com.example.demo.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.DossierMedical;
import com.example.demo.Models.Patient;
import com.example.demo.Models.Utilisateur;
import com.example.demo.Models.Role;
import com.example.demo.Repositories.DossierMedicalRepository;
import com.example.demo.Repositories.UtilisateurRepository;

@Service
public class DossierMedicalService {

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    public DossierMedical createDossierMedical(DossierMedical dossierMedical, Long patientId) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(patientId);
        
        if (utilisateurOpt.isPresent() && utilisateurOpt.get().getRole() == Role.PATIENT) {
            Patient patient = (Patient) utilisateurOpt.get();
            
            // Check if patient already has a medical record
            DossierMedical existingDossier = dossierMedicalRepository.findByPatient(patient);
            
            if (existingDossier != null) {
                // Update existing dossier
                existingDossier.setDiagnostic(dossierMedical.getDiagnostic());
                existingDossier.setPrescription(dossierMedical.getPrescription());
                return dossierMedicalRepository.save(existingDossier);
            } else {
                // Create new dossier
                dossierMedical.setPatient(patient);
                dossierMedical.setDateCreation(LocalDate.now());
                return dossierMedicalRepository.save(dossierMedical);
            }
        }
        
        return null;
    }
    
    public DossierMedical updateDossierMedical(DossierMedical dossierMedical) {
        return dossierMedicalRepository.save(dossierMedical);
    }
    
    public DossierMedical getDossierMedicalById(Long id) {
        return dossierMedicalRepository.findById(id).orElse(null);
    }
    
    public DossierMedical getDossierMedicalByPatient(Long patientId) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(patientId);
        
        if (utilisateurOpt.isPresent() && utilisateurOpt.get().getRole() == Role.PATIENT) {
            return dossierMedicalRepository.findByPatient((Patient) utilisateurOpt.get());
        }
        
        return null;
    }
    
    public List<DossierMedical> getAllDossiersMedicaux() {
        return dossierMedicalRepository.findAll();
    }
}
