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
import com.example.ldrp.medeeasyapp.listener.LaboratoryItemClickListener;
import com.example.ldrp.medeeasyapp.model.DoctorModel;
import com.example.ldrp.medeeasyapp.model.LaboratoryModel;

import java.util.ArrayList;

public class LaboratoryListAdapter extends RecyclerView.Adapter<LaboratoryListAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<LaboratoryModel> laboratoryModelArrayList;
    private LaboratoryItemClickListener laboratoryItemClickListener;


    public LaboratoryListAdapter(Context context, ArrayList<LaboratoryModel> laboratoryModelArrayList, LaboratoryItemClickListener laboratoryItemClickListener) {
        this.context = context;
        this.laboratoryModelArrayList = laboratoryModelArrayList;
        this.laboratoryItemClickListener = laboratoryItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LaboratoryModel laboratoryModel;
        private TextView nameTv;
        private TextView emailTv;
        private TextView mobileTv;
        private TextView addressTv;
        private TextView typesTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.row_layout_laboratory_list_name);
            emailTv = itemView.findViewById(R.id.row_layout_laboratory_list_email);
            mobileTv = itemView.findViewById(R.id.row_layout_laboratory_list_mobile);
            addressTv = itemView.findViewById(R.id.row_layout_laboratory_list_adress);
            typesTv = itemView.findViewById(R.id.row_layout_laboratory_list_types);

            itemView.setOnClickListener(this);
        }

        public void setData(LaboratoryModel laboratoryModel) {
            this.laboratoryModel = laboratoryModel;
            nameTv.setText(laboratoryModel.getName());
            emailTv.setText(laboratoryModel.getEmail());
            mobileTv.setText(laboratoryModel.getMobile());
            addressTv.setText(laboratoryModel.getAddress());
            typesTv.setText(laboratoryModel.getServices());
        }

        @Override
        public void onClick(View v) {
            if (laboratoryItemClickListener != null) {
                laboratoryItemClickListener.onLaboratoryItemClickListener(laboratoryModel, v);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout_laboratory_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        LaboratoryModel laboratoryModel = laboratoryModelArrayList.get(i);
        myViewHolder.setData(laboratoryModel);
    }


    @Override
    public int getItemCount() {
        return laboratoryModelArrayList.size();
    }

}
