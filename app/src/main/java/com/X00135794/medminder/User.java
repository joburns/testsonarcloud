package com.X00135794.medminder;

import java.util.ArrayList;
import java.util.Calendar;

public class User {
  private String firstName, lastName, gender, email, phone, county;
  private Calendar dob;
  private ArrayList<Medication> medList;

  public User() {
  }

  public User(String firstName, String lastName, String email, String phone, String county) {
    this.firstName = firstName;
    this.lastName = lastName;
    //this.gender = gender;
    this.email = email;
    this.phone = phone;
    this.county = county;
    //this.dob = dob;
    this.medList = new ArrayList<>();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getCounty() {
    return county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public Calendar getDob() {
    return dob;
  }

  public void setDob(Calendar dob) {
    this.dob = dob;
  }

  public ArrayList<Medication> getMedList() {
    return medList;
  }

  public void setMedList(ArrayList<Medication> medList) {
    this.medList = medList;
  }

  public void addMed(Medication med){
    int index = -1;
    if(medList.isEmpty()){
      medList.add(med);
    }
    else{
      for (Medication m: medList)
      {
        if(med.getMedName().equals(m.getMedName())){
          index = medList.indexOf(m);
        }
      }
      if(index != -1){
        medList.set(index,med);
      }else{
        medList.add(med);
      }
    }

  }
  public void removeMed(Medication med){
    int index = -1;
    if(medList.isEmpty()) {
      return;
    } else{
      for (Medication m: medList)
      {
        if(med.getMedName().equals(m.getMedName())){
          index = medList.indexOf(m);
        }
      }
      if(index != -1){
        medList.remove(med);
      }
    }
  }
}