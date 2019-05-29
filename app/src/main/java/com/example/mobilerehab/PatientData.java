package com.example.mobilerehab;

public class PatientData {

    String patient_name;
    String patient_icnumber;
    String patient_address;
    String patient_phonenumber;
    String patient_email;
    String patient_startdate;

    public PatientData() {

    }

    public PatientData(String patient_name, String patient_icnumber, String patient_address, String patient_phonenumber, String patient_email, String patient_startdate) {

        this.patient_name = patient_name;
        this.patient_icnumber = patient_icnumber;
        this.patient_address = patient_address;
        this.patient_phonenumber = patient_phonenumber;
        this.patient_email = patient_email;
        this.patient_startdate = patient_startdate;

    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_icnumber() {
        return patient_icnumber;
    }

    public void setPatient_icnumber(String patient_icnumber) {
        this.patient_icnumber = patient_icnumber;
    }

    public String getPatient_address() {
        return patient_address;
    }

    public void setPatient_address(String patient_address) {
        this.patient_address = patient_address;
    }

    public String getPatient_phonenumber() {
        return patient_phonenumber;
    }

    public void setPatient_phonenumber(String patient_phonenumber) {
        this.patient_phonenumber = patient_phonenumber;
    }

    public String getPatient_email() {
        return patient_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public String getPatient_startdate() {
        return patient_startdate;
    }

    public void setPatient_startdate(String patient_startdate) {
        this.patient_startdate = patient_startdate;
    }

}
