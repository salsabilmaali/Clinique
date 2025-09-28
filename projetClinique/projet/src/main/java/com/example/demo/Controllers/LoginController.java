package com.example.demo.Controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Utilisateur;
import com.example.demo.Models.Role;
import com.example.demo.Models.Specialite;
import com.example.demo.Services.LoginService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // retourne login.html
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String motDePasse,
                                   HttpSession session) {
        System.out.println("Login attempt for email: " + email); // Debug log
        
        try {
            Optional<Utilisateur> optionalUser = loginService.authenticate(email, motDePasse);

            if (optionalUser.isPresent()) {
                Utilisateur utilisateur = optionalUser.get();
                loginService.setSessionUser(session, utilisateur);
                System.out.println("Login successful for user: " + utilisateur.getEmail() + " with role: " + utilisateur.getRole());

                // Create a simplified user object for the response
                Map<String, Object> userResponse = loginService.createUserResponse(utilisateur);
                
                // Return user info as JSON
                return ResponseEntity.ok()
                        .header("Set-Cookie", "JSESSIONID=" + session.getId() + "; Path=/; SameSite=None; Secure")
                        .body(userResponse);
            } else {
                System.out.println("Login failed: Invalid credentials for email: " + email);
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error during login");
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        loginService.logout(session);
        return "redirect:/login";
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String email,
            @RequestParam String motDePasse,
            @RequestParam Role role,
            @RequestParam(required = false) Specialite specialite) {
        
        try {
            // Register the user using the service
            loginService.registerUser(nom, prenom, email, motDePasse, role, specialite);
            return ResponseEntity.ok().body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during registration: " + e.getMessage());
        }
    }
}
