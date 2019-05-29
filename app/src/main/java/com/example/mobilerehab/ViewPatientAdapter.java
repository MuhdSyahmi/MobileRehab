package com.example.mobilerehab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViewPatientAdapter extends RecyclerView.Adapter<ViewPatientAdapter.ViewHolder> {

    private Context context;
    private List<ViewPatientData> list;
    private OnPatientListener onPatientListener;

    public ViewPatientAdapter(Context context, List<ViewPatientData> list, OnPatientListener onPatientListener) {

        this.context = context;
        this.list = list;
        this.onPatientListener = onPatientListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_viewpatient, viewGroup, false);
        return new ViewHolder(view, onPatientListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPatientAdapter.ViewHolder viewHolder, int position) {
        ViewPatientData viewPatientData = list.get(position);
        viewHolder.textView_patientid.setText(String.valueOf(viewPatientData.getUser_id()));
        viewHolder.textView_patientname.setText(viewPatientData.getPatient_name());
        viewHolder.textView_patienticnumber.setText(viewPatientData.getPatient_icnumber());
        viewHolder.textView_patientaddress.setText(viewPatientData.getPatient_address());
        viewHolder.textView_patientphonenumber.setText(viewPatientData.getPatient_phonenumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnPatientListener {
        void onPatientClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView_patientid, textView_patientname, textView_patienticnumber, textView_patientaddress, textView_patientphonenumber;
        OnPatientListener onPatientListener;

        public ViewHolder(View view, OnPatientListener onPatientListener) {

            super(view);

            textView_patientid = view.findViewById(R.id.textView_patient_id);
            textView_patientname = view.findViewById(R.id.textView_patient_name);
            textView_patienticnumber = view.findViewById(R.id.textView_patienticnumber);
            textView_patientaddress = view.findViewById(R.id.textView_patientaddress);
            textView_patientphonenumber = view.findViewById(R.id.textView_patientphonenumber);
            this.onPatientListener = onPatientListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            onPatientListener.onPatientClick(getAdapterPosition());

        }
    }
}
