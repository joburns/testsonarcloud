package com.X00135794.medminder;

public class Medication {
    private String medName;
    private double dosage, frequency;

    public Medication(){

    }

    public Medication(String medName, double dosage, double frequency) {
        this.medName = medName;
        this.dosage = dosage;
        this.frequency = frequency;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}
