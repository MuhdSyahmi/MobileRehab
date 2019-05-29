package com.example.mobilerehab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViewAppointmentAdapter extends RecyclerView.Adapter<ViewAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<ViewAppointmentData> viewAppointmentDataList;
    private OnAppointmentListener onAppointmentListener;

    public ViewAppointmentAdapter(Context context, List<ViewAppointmentData> viewAppointmentDataList, OnAppointmentListener onAppointmentListener) {

        this.context = context;
        this.viewAppointmentDataList = viewAppointmentDataList;
        this.onAppointmentListener = onAppointmentListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_viewappointment, viewGroup, false);
        return new ViewHolder(view, onAppointmentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAppointmentAdapter.ViewHolder viewHolder, int position) {
        ViewAppointmentData viewAppointmentData = viewAppointmentDataList.get(position);
        viewHolder.textView_appointmentid.setText(viewAppointmentData.getAppointment_id());
        viewHolder.textView_patient_name.setText(viewAppointmentData.getPatient_name());
        viewHolder.textView_appointment_time.setText(viewAppointmentData.getAppointment_time());
        viewHolder.textView_appointment_date.setText(viewAppointmentData.getAppointment_date());
        viewHolder.textView_appointmentstatus.setText(viewAppointmentData.getAppointment_status());
    }

    @Override
    public int getItemCount() {
        return viewAppointmentDataList.size();
    }

    public interface OnAppointmentListener {
        void onAppointmentClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView_userid, textView_appointmentid, textView_patient_name, textView_appointment_time, textView_appointment_date, textView_appointmentstatus;
        OnAppointmentListener onAppointmentListener;

        public ViewHolder(View view, OnAppointmentListener onAppointmentListener) {
            super(view);

            textView_userid = view.findViewById(R.id.textView_userid);
            textView_appointmentid = view.findViewById(R.id.textView_patient_appointmentid);
            textView_patient_name = view.findViewById(R.id.textView_patient_name);
            textView_appointment_time = view.findViewById(R.id.textView_patient_appointmenttime);
            textView_appointment_date = view.findViewById(R.id.textView_patient_appointmentdate);
            textView_appointmentstatus = view.findViewById(R.id.textView_patient_appointmentstatus);

            this.onAppointmentListener = onAppointmentListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAppointmentListener.onAppointmentClick(getAdapterPosition());
        }
    }

}
