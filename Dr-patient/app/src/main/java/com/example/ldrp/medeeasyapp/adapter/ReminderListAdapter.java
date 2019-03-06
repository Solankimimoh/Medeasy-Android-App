package com.example.ldrp.medeeasyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ldrp.medeeasyapp.R;
import com.example.ldrp.medeeasyapp.listener.ReminderItemClickListener;
import com.example.ldrp.medeeasyapp.listener.ReportItemClickListener;
import com.example.ldrp.medeeasyapp.model.PatientReviewModel;
import com.example.ldrp.medeeasyapp.model.ReminderModel;

import java.util.ArrayList;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<ReminderModel> reminderModelArrayList;
    private ReminderItemClickListener reminderItemClickListener;

    public ReminderListAdapter(Context context, ArrayList<ReminderModel> reminderModelArrayList, ReminderItemClickListener reminderItemClickListener) {
        this.context = context;
        this.reminderModelArrayList = reminderModelArrayList;
        this.reminderItemClickListener = reminderItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ReminderModel reminderModel;
        private TextView titleTv;
        private TextView descriptionTv;
        private TextView timeTv;
        private TextView dateTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.row_layout_reminder_title);
            descriptionTv = itemView.findViewById(R.id.row_layout_reminder_description);
            timeTv = itemView.findViewById(R.id.row_layout_reminder_time);
            dateTv = itemView.findViewById(R.id.row_layout_reminder_date);

            itemView.setOnClickListener(this);
        }

        public void setData(ReminderModel data) {
            reminderModel = data;
            titleTv.setText(data.getTitle());
            descriptionTv.setText(data.getDescription());
            timeTv.setText(data.getTime());
            dateTv.setText(data.getDate());
        }

        @Override
        public void onClick(View v) {
            if (reminderItemClickListener != null) {
                reminderItemClickListener.onReminderItemClick(reminderModel);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout_reminder, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        ReminderModel reminderModel = reminderModelArrayList.get(i);
        myViewHolder.setData(reminderModel);
    }


    @Override
    public int getItemCount() {
        return reminderModelArrayList.size();
    }

}
