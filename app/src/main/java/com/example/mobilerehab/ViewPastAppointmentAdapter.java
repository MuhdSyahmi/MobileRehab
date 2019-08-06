package com.example.mobilerehab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViewPastAppointmentAdapter extends RecyclerView.Adapter<ViewPastAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<ViewPastAppointmentData> viewPastAppointmentDataList;
    private OnPastAppointmentListener onPastAppointmentListener;

    public ViewPastAppointmentAdapter(Context context, List<ViewPastAppointmentData> viewPastAppointmentDataList, OnPastAppointmentListener onPastAppointmentListener) {
        this.context = context;
        this.viewPastAppointmentDataList = viewPastAppointmentDataList;
        this.onPastAppointmentListener = onPastAppointmentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_viewpastappointment, viewGroup, false);
        return new ViewHolder(view, onPastAppointmentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPastAppointmentAdapter.ViewHolder viewHolder, int position) {
        ViewPastAppointmentData viewPastAppointmentData = viewPastAppointmentDataList.get(position);
        viewHolder.textView_appointmentdate.setText(viewPastAppointmentData.getAppointmentDate());
        viewHolder.textView_appointmenttime.setText(viewPastAppointmentData.getAppointmentTime());
    }

    @Override
    public int getItemCount() {
        return viewPastAppointmentDataList.size();
    }

    public interface OnPastAppointmentListener {
        void onPastAppointmentClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView_appointmentdate, textView_appointmenttime;
        OnPastAppointmentListener onPastAppointmentListener;

        public ViewHolder(View view, OnPastAppointmentListener onPastAppointmentListener) {
            super(view);

            textView_appointmentdate = view.findViewById(R.id.textView_appointmentdate);
            textView_appointmenttime = view.findViewById(R.id.textView_appointmenttime);

            this.onPastAppointmentListener = onPastAppointmentListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPastAppointmentListener.onPastAppointmentClick(getAdapterPosition());
        }
    }
}