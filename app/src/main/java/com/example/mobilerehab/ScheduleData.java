package com.example.mobilerehab;

public class ScheduleData {

    public int user_id;
    public int schedule_id;
    public String schedule_patientname;
    public String schedule_date;
    public String schedule_time;

    public ScheduleData(){

    }

    public ScheduleData(int user_id, int schedule_id, String schedule_patientname, String schedule_date, String schedule_time) {
        this.user_id = user_id;
        this.schedule_id = schedule_id;
        this.schedule_patientname = schedule_patientname;
        this.schedule_date = schedule_date;
        this.schedule_time = schedule_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getSchedule_patientname() {
        return schedule_patientname;
    }

    public void setSchedule_patientname(String schedule_patientname) {
        this.schedule_patientname = schedule_patientname;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }
}
