package com.X00135794.medminder;

public class Medication {
    private String medName;
    private String dosageType, freqRate;
    private double dosage, frequency;

    public Medication() {

    }

    public Medication(String medName, double dosage, double frequency) {
        this.medName = medName;
        this.dosage = dosage;
        this.frequency = frequency;
    }

    public Medication(String medName, String dosageType, double dosage, String freqRate, double frequency) {
        this.medName = medName;
        this.dosageType = dosageType;
        this.dosage = dosage;
        this.freqRate = freqRate;
        this.frequency = frequency;
    }

    public String getMedName() {
        return medName;
    }
    public String getDosageType() {
        return dosageType;
    }

    public String getFreqRate() {
        return freqRate;
    }
    public void setMedName(String medName) {
        this.medName = medName;
    }

    public double getDosage() {
        return dosage;
    }

    public String getDosageString() {
        String dosageString = Double.toString(dosage);
        return dosageString;
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
    public String getFrequencyString() {
        String freqString = Double.toString(frequency);
        return freqString;
    }
}
