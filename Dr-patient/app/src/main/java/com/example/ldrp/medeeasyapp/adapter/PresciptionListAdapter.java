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
import com.example.ldrp.medeeasyapp.listener.DoctorItemClickListener;
import com.example.ldrp.medeeasyapp.listener.PrescriptionItemClickListener;
import com.example.ldrp.medeeasyapp.model.DoctorModel;
import com.example.ldrp.medeeasyapp.model.PrescriptionModel;

import java.util.ArrayList;

public class PresciptionListAdapter extends RecyclerView.Adapter<PresciptionListAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<PrescriptionModel> prescriptionModelArrayList;
    private PrescriptionItemClickListener prescriptionItemClickListener;


    public PresciptionListAdapter(Context context, ArrayList<PrescriptionModel> prescriptionModelArrayList, PrescriptionItemClickListener prescriptionItemClickListener) {
        this.context = context;
        this.prescriptionModelArrayList = prescriptionModelArrayList;
        this.prescriptionItemClickListener = prescriptionItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PrescriptionModel prescriptionModel;
        private TextView titleTv;
        private TextView descriptionTv;
        private TextView date;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.row_layout_presciption_title);
            descriptionTv = itemView.findViewById(R.id.row_layout_presciption_description);
            date = itemView.findViewById(R.id.row_layout_presciption_date);
            imageView = itemView.findViewById(R.id.row_layout_presciption_img);

            itemView.setOnClickListener(this);
        }

        public void setData(PrescriptionModel data) {
            this.prescriptionModel = data;
            titleTv.setText(data.getTitle());
            descriptionTv.setText(data.getDescription());
            date.setText(data.getDate());


            Glide.with(context)
                    .load(data.getImgUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(imageView);

        }

        @Override
        public void onClick(View v) {
            if (prescriptionItemClickListener != null) {
                prescriptionItemClickListener.onPrescriptionItemClick(prescriptionModel);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_prescription, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        PrescriptionModel prescriptionModel = prescriptionModelArrayList.get(i);
        myViewHolder.setData(prescriptionModel);
    }


    @Override
    public int getItemCount() {
        return prescriptionModelArrayList.size();
    }

}
