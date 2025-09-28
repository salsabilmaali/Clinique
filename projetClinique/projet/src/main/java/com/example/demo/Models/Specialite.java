package com.example.demo.Models;

/**
 * Enum representing medical specialties for doctors
 */
public enum Specialite {
    CARDIOLOGIE("Cardiologie"),
    DERMATOLOGIE("Dermatologie"),
    GASTROENTEROLOGIE("Gastroentérologie"),
    GYNECOLOGIE("Gynécologie"),
    NEUROLOGIE("Neurologie"),
    OPHTALMOLOGIE("Ophtalmologie"),
    ORTHOPEDIE("Orthopédie"),
    PEDIATRIE("Pédiatrie"),
    PSYCHIATRIE("Psychiatrie"),
    RADIOLOGIE("Radiologie"),
    UROLOGIE("Urologie"),
    GENERALISTE("Médecine Générale");
    
    private final String displayName;
    
    Specialite(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
}
