package com.example.mobilerehab;

import com.google.gson.annotations.SerializedName;

public class SpinnerData {

    @SerializedName("name")
    private int patient_id;
    private String patient_name;

    public SpinnerData() {

    }

    public SpinnerData(int patient_id, String patient_name) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

}
