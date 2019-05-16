package com.example.mobilerehab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PatientViewAppointmentAdapter extends RecyclerView.Adapter<PatientViewAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<PatientViewAppointmentData> patientViewAppointmentDataList;
    private OnPatientAppointmentListener onPatientAppointmentListener;

    public PatientViewAppointmentAdapter(Context context, List<PatientViewAppointmentData> patientViewAppointmentDataList, OnPatientAppointmentListener onPatientAppointmentListener) {
        this.context = context;
        this.patientViewAppointmentDataList = patientViewAppointmentDataList;
        this.onPatientAppointmentListener = onPatientAppointmentListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_patientviewappointment, viewGroup, false);
        return new ViewHolder(view, onPatientAppointmentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewAppointmentAdapter.ViewHolder viewHolder, int position) {
        PatientViewAppointmentData patientViewAppointmentData = patientViewAppointmentDataList.get(position);
        viewHolder.textView_userid.setText(String.valueOf(patientViewAppointmentData.getUser_id()));
        viewHolder.textView_appointmentid.setText(String.valueOf(patientViewAppointmentData.getAppointment_id()));
        viewHolder.textView_doctorid.setText(String.valueOf(patientViewAppointmentData.getDoctor_id()));
        viewHolder.textView_patient_name.setText(patientViewAppointmentData.getPatient_name());
        viewHolder.textView_appointment_time.setText(patientViewAppointmentData.getAppointment_time());
        viewHolder.textView_appointment_date.setText(patientViewAppointmentData.getAppointment_date());
    }

    @Override
    public int getItemCount() {
        return patientViewAppointmentDataList.size();
    }

    public interface OnPatientAppointmentListener {
        void onPatientAppointmentClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView_userid, textView_appointmentid, textView_doctorid, textView_patient_name, textView_appointment_time, textView_appointment_date;
        OnPatientAppointmentListener onPatientAppointmentListener;

        public ViewHolder(View view, OnPatientAppointmentListener onPatientAppointmentListener) {
            super(view);
            textView_userid = view.findViewById(R.id.textView_userid);
            textView_appointmentid = view.findViewById(R.id.textView_appointmentid);
            textView_doctorid = view.findViewById(R.id.textView_doctorid);
            textView_patient_name = view.findViewById(R.id.textView_patient_name);
            textView_appointment_time = view.findViewById(R.id.textView_patient_appointmenttime);
            textView_appointment_date = view.findViewById(R.id.textView_patient_appointmentdate);
            this.onPatientAppointmentListener = onPatientAppointmentListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPatientAppointmentListener.onPatientAppointmentClick(getAdapterPosition());
        }
    }
}
