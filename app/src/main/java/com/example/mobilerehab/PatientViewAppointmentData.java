package com.example.mobilerehab;

public class PatientViewAppointmentData {

    public int user_id;
    public int appointment_id;
    public int doctor_id;
    public String patient_name;
    public String appointment_time;
    public String appointment_date;

    public PatientViewAppointmentData() {

    }

    public PatientViewAppointmentData(int user_id, int appointment_id, String patient_name, String appointment_time, String appointment_date) {
        this.user_id = user_id;
        this.appointment_id = appointment_id;
        this.patient_name = patient_name;
        this.appointment_time = appointment_time;
        this.appointment_date = appointment_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }


    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

}
