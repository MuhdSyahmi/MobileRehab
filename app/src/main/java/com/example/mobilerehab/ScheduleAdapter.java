package com.example.mobilerehab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Context context;
    private List<ScheduleData> list;
    private OnScheduleListener onScheduleListener;

    public ScheduleAdapter(Context context, List<ScheduleData> list, OnScheduleListener onScheduleListener){
        this.context = context;
        this.list = list;
        this.onScheduleListener = onScheduleListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_schedule, viewGroup, false);
        return new ViewHolder(view, onScheduleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ScheduleData scheduleData = list.get(position);
        viewHolder.tv_scheduleid.setText(String.valueOf(scheduleData.getSchedule_id()));
        viewHolder.tv_patientname.setText(scheduleData.getSchedule_patientname());
        viewHolder.tv_scheduledate.setText(scheduleData.getSchedule_date());
        viewHolder.tv_scheduletime.setText(scheduleData.getSchedule_time());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_patientname, tv_scheduledate, tv_scheduletime, tv_scheduleid;
        OnScheduleListener onScheduleListener;


        public ViewHolder(View view, OnScheduleListener onScheduleListener){
            super(view);

            tv_scheduleid = view.findViewById(R.id.textView_scheduleid);
            tv_patientname = view.findViewById(R.id.textView_patient_name);
            tv_scheduledate = view.findViewById(R.id.textView_scheduledate);
            tv_scheduletime = view.findViewById(R.id.textView_scheduletime);
            this.onScheduleListener = onScheduleListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onScheduleListener.onScheduleClick(getAdapterPosition());
        }
    }

    public interface OnScheduleListener{
        void onScheduleClick(int position);
    }
}
