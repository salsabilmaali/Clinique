package com.example.demo.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;

@Entity
public class Medecin extends Utilisateur {

    @Enumerated(EnumType.STRING)
    private Specialite specialite;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RendezVous> rendezVous;

	public Specialite getSpecialite() {
		return specialite;
	}

	public void setSpecialite(Specialite specialite) {
		this.specialite = specialite;
	}

	public List<RendezVous> getRendezVous() {
		return rendezVous;
	}

	public void setRendezVous(List<RendezVous> rendezVous) {
		this.rendezVous = rendezVous;
	}
    
    
}
