package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

	Optional<Utilisateur> findByEmailAndMotDePasse(String email, String motDePasse);
	
	Optional<Utilisateur> findByEmail(String email);

}
