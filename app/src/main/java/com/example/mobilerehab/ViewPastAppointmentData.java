package com.example.mobilerehab;

public class ViewPastAppointmentData {

    public String appointmentDate;
    public String appointmentTime;

    public ViewPastAppointmentData() {

    }

    public ViewPastAppointmentData(String appointmentDate, String appointmentTime) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

}
