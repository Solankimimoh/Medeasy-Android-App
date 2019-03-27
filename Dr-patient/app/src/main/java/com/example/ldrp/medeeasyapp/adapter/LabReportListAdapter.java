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
import com.example.ldrp.medeeasyapp.listener.LabReportItemClickListener;
import com.example.ldrp.medeeasyapp.listener.PrescriptionItemClickListener;
import com.example.ldrp.medeeasyapp.model.LabReportModel;
import com.example.ldrp.medeeasyapp.model.PrescriptionModel;

import java.util.ArrayList;

public class LabReportListAdapter extends RecyclerView.Adapter<LabReportListAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<LabReportModel> labReportModelArrayList;
    private LabReportItemClickListener labReportItemClickListener;

    public LabReportListAdapter(Context context, ArrayList<LabReportModel> labReportModelArrayList, LabReportItemClickListener labReportItemClickListener) {
        this.context = context;
        this.labReportModelArrayList = labReportModelArrayList;
        this.labReportItemClickListener = labReportItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LabReportModel labReportModel;
        private TextView titleTv;
        private TextView descriptionTv;
        private TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.row_layout_lab_report_title);
            descriptionTv = itemView.findViewById(R.id.row_layout_lab_report_description);
            date = itemView.findViewById(R.id.row_layout_lab_report_date);

            itemView.setOnClickListener(this);
        }

        public void setData(LabReportModel data) {
            this.labReportModel = data;
            titleTv.setText(data.getTitle());
            descriptionTv.setText(data.getDescription());
            date.setText(data.getDate());


        }

        @Override
        public void onClick(View v) {
            if (labReportItemClickListener != null) {
                labReportItemClickListener.onLabReportItemClick(labReportModel);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_lab_report_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        LabReportModel labReportModel = labReportModelArrayList.get(i);
        myViewHolder.setData(labReportModel);
    }


    @Override
    public int getItemCount() {
        return labReportModelArrayList.size();
    }

}
