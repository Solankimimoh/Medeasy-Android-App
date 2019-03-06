package com.example.ldrp.medeeasyapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ldrp.medeeasyapp.R;
import com.example.ldrp.medeeasyapp.listener.DoctorItemClickListener;
import com.example.ldrp.medeeasyapp.model.DoctorModel;

import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<DoctorModel> doctorModelArrayList;
    private DoctorItemClickListener doctorItemClickListener;


    public DoctorListAdapter(Context context, ArrayList<DoctorModel> doctorModelArrayList, DoctorItemClickListener doctorItemClickListener) {
        this.context = context;
        this.doctorModelArrayList = doctorModelArrayList;
        this.doctorItemClickListener = doctorItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private DoctorModel doctorModel;
        private TextView nameTv;
        private TextView emailTv;
        private TextView mobileTv;
        private TextView addressTv;
        private TextView educationTv;
        private TextView typesTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.row_layout_doctor_list_name);
            emailTv = itemView.findViewById(R.id.row_layout_doctor_list_email);
            mobileTv = itemView.findViewById(R.id.row_layout_doctor_list_mobile);
            addressTv = itemView.findViewById(R.id.row_layout_doctor_list_adress);
            educationTv = itemView.findViewById(R.id.row_layout_doctor_list_education);
            typesTv = itemView.findViewById(R.id.row_layout_doctor_list_types);

            itemView.setOnClickListener(this);
        }

        public void setData(DoctorModel doctorModel) {
            this.doctorModel = doctorModel;
            nameTv.setText(doctorModel.getName());
            emailTv.setText(doctorModel.getEmail());
            mobileTv.setText(doctorModel.getMobile());
            addressTv.setText(doctorModel.getAddress());
            educationTv.setText(doctorModel.getEducation());
            typesTv.setText(doctorModel.getTypes());
        }

        @Override
        public void onClick(View v) {
            if (doctorItemClickListener != null) {
                doctorItemClickListener.onDoctorItemClick(doctorModel,v);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_doctor_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        DoctorModel doctorModel = doctorModelArrayList.get(i);
        myViewHolder.setData(doctorModel);
    }


    @Override
    public int getItemCount() {
        return doctorModelArrayList.size();
    }

}
