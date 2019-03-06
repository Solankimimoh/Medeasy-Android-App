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
import com.example.ldrp.medeeasyapp.listener.MedicalListtemClickListener;
import com.example.ldrp.medeeasyapp.model.DoctorModel;
import com.example.ldrp.medeeasyapp.model.MedicalModel;

import java.util.ArrayList;

public class MedicalListAdapter extends RecyclerView.Adapter<MedicalListAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<MedicalModel> medicalModelArrayList;
    private MedicalListtemClickListener medicalListtemClickListener;

    public MedicalListAdapter(Context context, ArrayList<MedicalModel> medicalModelArrayList, MedicalListtemClickListener medicalListtemClickListener) {
        this.context = context;
        this.medicalModelArrayList = medicalModelArrayList;
        this.medicalListtemClickListener = medicalListtemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MedicalModel medicalModel;
        private TextView nameTv;
        private TextView emailTv;
        private TextView mobileTv;
        private TextView addressTv;
        private TextView typesTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.row_layout_medical_list_name);
            emailTv = itemView.findViewById(R.id.row_layout_medical_list_email);
            mobileTv = itemView.findViewById(R.id.row_layout_medical_list_mobile);
            addressTv = itemView.findViewById(R.id.row_layout_medical_list_adress);
            typesTv = itemView.findViewById(R.id.row_layout_medical_list_types);

            itemView.setOnClickListener(this);
        }

        public void setData(MedicalModel data) {
            this.medicalModel = data;
            nameTv.setText(data.getName());
            emailTv.setText(data.getEmail());
            mobileTv.setText(data.getMobile());
            addressTv.setText(data.getAddress());
            typesTv.setText(data.getTypes());
        }

        @Override
        public void onClick(View v) {
            if (medicalListtemClickListener != null) {
                medicalListtemClickListener.onMedicalListtemClick(medicalModel, v);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_medical_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        MedicalModel medicalModel= medicalModelArrayList.get(i);
        myViewHolder.setData(medicalModel);
    }


    @Override
    public int getItemCount() {
        return medicalModelArrayList.size();
    }

}
