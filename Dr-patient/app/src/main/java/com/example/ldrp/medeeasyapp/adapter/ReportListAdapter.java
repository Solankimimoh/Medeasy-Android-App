package com.example.ldrp.medeeasyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ldrp.medeeasyapp.R;
import com.example.ldrp.medeeasyapp.listener.PrescriptionItemClickListener;
import com.example.ldrp.medeeasyapp.listener.ReportItemClickListener;
import com.example.ldrp.medeeasyapp.model.PatientReviewModel;
import com.example.ldrp.medeeasyapp.model.PrescriptionModel;

import java.util.ArrayList;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<PatientReviewModel> patientReviewModelArrayList;
    private ReportItemClickListener reportItemClickListener;

    public ReportListAdapter(Context context, ArrayList<PatientReviewModel> patientReviewModelArrayList, ReportItemClickListener reportItemClickListener) {
        this.context = context;
        this.patientReviewModelArrayList = patientReviewModelArrayList;
        this.reportItemClickListener = reportItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PatientReviewModel reviewModel;
        private TextView titleTv;
        private TextView descriptionTv;
        private TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.row_layout_presciption_title);
            descriptionTv = itemView.findViewById(R.id.row_layout_presciption_description);
            date = itemView.findViewById(R.id.row_layout_presciption_description_date);

            itemView.setOnClickListener(this);
        }

        public void setData(PatientReviewModel data) {
            reviewModel = data;
            titleTv.setText(data.getTitle());
            descriptionTv.setText(data.getDescription());
            date.setText(data.getDate());

        }

        @Override
        public void onClick(View v) {
            if (reportItemClickListener != null) {
                reportItemClickListener.onReportItemClick(reviewModel);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout_report, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        PatientReviewModel patientReviewModel = patientReviewModelArrayList.get(i);
        myViewHolder.setData(patientReviewModel);
    }


    @Override
    public int getItemCount() {
        return patientReviewModelArrayList.size();
    }

}
