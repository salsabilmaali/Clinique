package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Models.DossierMedical;
import com.example.demo.Services.DossierMedicalService;

import java.util.List;

@RestController
@RequestMapping("/api/dossiers")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class DossierMedicalController {

    @Autowired
    private DossierMedicalService dossierMedicalService;
    
    @PostMapping("/create/{patientId}")
    public ResponseEntity<?> createDossierMedical(@RequestBody DossierMedical dossierMedical, @PathVariable Long patientId) {
        DossierMedical createdDossier = dossierMedicalService.createDossierMedical(dossierMedical, patientId);
        
        if (createdDossier != null) {
            return new ResponseEntity<>(createdDossier, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Patient not found or error creating medical record", HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getDossierMedicalByPatient(@PathVariable Long patientId) {
        DossierMedical dossierMedical = dossierMedicalService.getDossierMedicalByPatient(patientId);
        
        if (dossierMedical != null) {
            return new ResponseEntity<>(dossierMedical, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Medical record not found for this patient", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getDossierMedicalById(@PathVariable Long id) {
        DossierMedical dossierMedical = dossierMedicalService.getDossierMedicalById(id);
        
        if (dossierMedical != null) {
            return new ResponseEntity<>(dossierMedical, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Medical record not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateDossierMedical(@RequestBody DossierMedical dossierMedical) {
        DossierMedical updatedDossier = dossierMedicalService.updateDossierMedical(dossierMedical);
        
        if (updatedDossier != null) {
            return new ResponseEntity<>(updatedDossier, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error updating medical record", HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<DossierMedical>> getAllDossiersMedicaux() {
        List<DossierMedical> dossiersMedicaux = dossierMedicalService.getAllDossiersMedicaux();
        return new ResponseEntity<>(dossiersMedicaux, HttpStatus.OK);
    }
}
