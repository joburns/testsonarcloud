package com.X00135794.medminder;

import java.util.ArrayList;
import java.util.Date;

public class Medication {

    private String medId;
    private String medName;
    private String dosageType, freqRate;
    private double dosage, frequency;
    private boolean active;
    private Date statDate;
    private ArrayList<Integer> alarmRequestCodes;
    private String description;

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
    public Medication(String medId, String medName, String dosageType, double dosage, String freqRate, double frequency, Date startDate, String description, ArrayList<Integer> alarmRequestCodes) {
        this.medId = medId;
        this.medName = medName;
        this.dosageType = dosageType;
        this.dosage = dosage;
        this.freqRate = freqRate;
        this.frequency = frequency;
        this.active = true;
        this.statDate = startDate;
        this.description = description;
        this.alarmRequestCodes = alarmRequestCodes;
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


    public ArrayList<Integer> getAlarmRequestCodes() {return alarmRequestCodes; }

    public void setAlarmRequestCodes(ArrayList<Integer> alarmRequestCodes) { this.alarmRequestCodes = alarmRequestCodes;  }

    public Date getStatDate() { return statDate; }

    public void setStatDate(Date statDate) { this.statDate = statDate;  }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}