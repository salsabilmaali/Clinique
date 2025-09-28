package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import com.example.demo.Models.Medecin;
import com.example.demo.Models.Patient;
import com.example.demo.Models.Role;
import com.example.demo.Models.Specialite;
import com.example.demo.Models.Utilisateur;
import com.example.demo.Services.UserService;
import com.example.demo.Services.LoginService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginService loginService;

    @GetMapping("/users")
    public List<Utilisateur> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/count")
    public long getUserCount() {
        return userService.getUserCount();
    }

    @GetMapping("/users/recent")
    public List<Utilisateur> getRecentUsers(Integer limit) {
        return userService.getRecentUsers(limit);
    }

    @GetMapping("/medecins")
    public List<Medecin> getAllMedecins() {
        return userService.getAllMedecins();
    }
    
    /**
     * Get doctors by specialty
     * @param specialite The specialty to filter by
     * @return List of doctors with the specified specialty
     */
    @GetMapping("/medecins/specialite/{specialite}")
    public List<Medecin> getMedecinsBySpecialite(@PathVariable String specialite) {
        return userService.getMedecinsBySpecialiteName(specialite);
    }

    @GetMapping("/medecins/active/count")
    public long getActiveMedecinsCount() {
        return userService.getActiveMedecinsCount();
    }

    @GetMapping("/patients")
    public List<Patient> getAllPatients() {
        return userService.getAllPatients();
    }

    @GetMapping("/patients/new/count")
    public long getNewPatientsCount(Integer days) {
        return userService.getNewPatientsCount(days);
    }
    
    @GetMapping("/patients/new")
    public List<Patient> getNewPatients(Integer days) {
        return userService.getNewPatients(days);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/current")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Optional<Utilisateur> utilisateur = loginService.getCurrentUser(session);
        if (utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            return ResponseEntity.status(401).body("Not authenticated");
        }
    }
    
    /**
     * Get all available medical specialties
     * @return List of all specialties as strings
     */
    @GetMapping("/specialites")
    public ResponseEntity<List<Map<String, String>>> getAllSpecialites() {
        List<Map<String, String>> specialites = new ArrayList<>();
        
        for (Specialite specialite : Specialite.values()) {
            Map<String, String> specialiteMap = new HashMap<>();
            specialiteMap.put("value", specialite.name());
            specialiteMap.put("displayName", specialite.getDisplayName());
            specialites.add(specialiteMap);
        }
        
        return ResponseEntity.ok(specialites);
    }
    
    /**
     * Update user information
     * @param id The ID of the user to update
     * @param updatedUserData The updated user data
     * @return ResponseEntity with the updated user or an error
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updatedUserData) {
        // Check if the user exists
        Optional<Utilisateur> existingUserOpt = userService.getUserById(id);
        if (!existingUserOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Utilisateur existingUser = existingUserOpt.get();
        
        // Update user fields from the request data
        if (updatedUserData.containsKey("nom")) {
            existingUser.setNom((String) updatedUserData.get("nom"));
        }
        
        if (updatedUserData.containsKey("prenom")) {
            existingUser.setPrenom((String) updatedUserData.get("prenom"));
        }
        
        if (updatedUserData.containsKey("email")) {
            existingUser.setEmail((String) updatedUserData.get("email"));
        }
        
        // Only update password if provided and not empty
        if (updatedUserData.containsKey("motDePasse") && updatedUserData.get("motDePasse") != null 
            && !((String) updatedUserData.get("motDePasse")).isEmpty()) {
            existingUser.setMotDePasse((String) updatedUserData.get("motDePasse"));
        }
        
        // Handle specialty for doctors
        if (updatedUserData.containsKey("specialite") && Role.MEDECIN.equals(existingUser.getRole())) {
            // If the user is a doctor, we need to cast to Medecin to set the specialty
            if (existingUser instanceof Medecin) {
                Medecin medecin = (Medecin) existingUser;
                String specialiteStr = (String) updatedUserData.get("specialite");
                try {
                    // Convert string to enum
                    Specialite specialite = Specialite.valueOf(specialiteStr.toUpperCase());
                    medecin.setSpecialite(specialite);
                } catch (IllegalArgumentException e) {
                    // Handle invalid specialty
                    return ResponseEntity.badRequest().body("Invalid specialty: " + specialiteStr);
                }
            }
        }
        
        // Save the updated user
        Utilisateur updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Update patient information
     * @param id The ID of the patient to update
     * @param updatedPatientData The updated patient data
     * @return ResponseEntity with the updated patient or an error
     */
    @PutMapping("/patients/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody Map<String, Object> updatedPatientData) {
        // Check if the patient exists
        Optional<Utilisateur> existingUserOpt = userService.getUserById(id);
        if (!existingUserOpt.isPresent() || !(existingUserOpt.get() instanceof Patient)) {
            return ResponseEntity.notFound().build();
        }
        
        Patient existingPatient = (Patient) existingUserOpt.get();
        
        // Update patient fields from the request data
        if (updatedPatientData.containsKey("nom")) {
            existingPatient.setNom((String) updatedPatientData.get("nom"));
        }
        
        if (updatedPatientData.containsKey("prenom")) {
            existingPatient.setPrenom((String) updatedPatientData.get("prenom"));
        }
        
        if (updatedPatientData.containsKey("email")) {
            existingPatient.setEmail((String) updatedPatientData.get("email"));
        }
        
        // Only update password if provided and not empty
        if (updatedPatientData.containsKey("motDePasse") && updatedPatientData.get("motDePasse") != null 
            && !((String) updatedPatientData.get("motDePasse")).isEmpty()) {
            existingPatient.setMotDePasse((String) updatedPatientData.get("motDePasse"));
        }
        
        // Save the updated patient
        Patient updatedPatient = (Patient) userService.saveUser(existingPatient);
        return ResponseEntity.ok(updatedPatient);
    }
}
