package com.example.mobilerehab;

public class ViewAppointmentData {

    public String appointment_id;
    public String patient_name;
    public String appointment_time;
    public String appointment_date;
    public String appointment_status;

    public ViewAppointmentData() {

    }

    public ViewAppointmentData(String appointment_id, String patient_name, String appointment_time, String appointment_date, String appointment_status) {

        this.appointment_id = appointment_id;
        this.patient_name = patient_name;
        this.appointment_time = appointment_time;
        this.appointment_date = appointment_date;
        this.appointment_status = appointment_status;

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

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(String appointment_status) {
        this.appointment_status = appointment_status;
    }

}
