package pja.kociespa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Medicine extends PersonalBelonging {
    @Column(nullable = false)
    private String recommendations;
    @Column(nullable = false)
    private Double dosage;
    @Column(nullable = false)
    private String administrationFrequency;

    public Medicine(String name, String recommendations, Double dosage, String administrationFrequency) {
        super(name);
        this.recommendations = recommendations;
        this.dosage = dosage;
        this.administrationFrequency = administrationFrequency;
    }
    protected Medicine() {}

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public Double getDosage() {
        return dosage;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }

    public String getAdministrationFrequency() {
        return administrationFrequency;
    }

    public void setAdministrationFrequency(String administrationFrequency) {
        this.administrationFrequency = administrationFrequency;
    }
}
