package com.X00135794.medminder;

import java.util.Date;

public class Medication {

    private String medId;
    private String medName;
    private String dosageType, freqRate;
    private double dosage, frequency;
    private boolean active;
    private Date statDate;

    //may add alarm request  inorder for deletion

    public Medication() {

    }

   // for array list of medication
    public Medication(String medName, String dosageType, double dosage, String freqRate, double frequency) {
        this.medName = medName;
        this.dosageType = dosageType;
        this.dosage = dosage;
        this.freqRate = freqRate;
        this.frequency = frequency;
        this.active = true;
    }
    public Medication(String medId, String medName, String dosageType, double dosage, String freqRate, double frequency, Date startDate) {
        this.medId = medId;
        this.medName = medName;
        this.dosageType = dosageType;
        this.dosage = dosage;
        this.freqRate = freqRate;
        this.frequency = frequency;
        this.active = true;
        this.statDate = startDate;
    }

    public String getMedId() { return medId; }

    public void setMedId(String medId) { this.medId = medId; }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}