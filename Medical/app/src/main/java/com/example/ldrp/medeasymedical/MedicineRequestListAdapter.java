package com.example.ldrp.medeasymedical;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MedicineRequestListAdapter extends RecyclerView.Adapter<MedicineRequestListAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<MedicineRequestModel> medicineRequestModelArrayList;
    private MedicineRequestItemClickListener medicineRequestItemClickListener;


    public MedicineRequestListAdapter(Context context, ArrayList<MedicineRequestModel> medicineRequestModelArrayList, MedicineRequestItemClickListener medicineRequestItemClickListener) {
        this.context = context;
        this.medicineRequestModelArrayList = medicineRequestModelArrayList;
        this.medicineRequestItemClickListener = medicineRequestItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MedicineRequestModel medicineRequestModel;
        private TextView titleTv;
        private TextView descriptionTv;
        private TextView nameTv;
        private TextView emailTv;
        private TextView mobileTv;
        private TextView addressTv;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.row_layout_medicine_title);
            descriptionTv = itemView.findViewById(R.id.row_layout_medicine_description);
            nameTv = itemView.findViewById(R.id.row_layout_medicine_name);
            emailTv = itemView.findViewById(R.id.row_layout_medicine_email);
            mobileTv = itemView.findViewById(R.id.row_layout_medicine_mobile);
            addressTv = itemView.findViewById(R.id.row_layout_medicine_address);
            imageView = itemView.findViewById(R.id.row_layout_medicine_img);

            itemView.setOnClickListener(this);
        }

        public void setData(MedicineRequestModel data) {
            medicineRequestModel = data;
            titleTv.setText(data.getTitle());
            descriptionTv.setText(data.getDescription());
            nameTv.setText(data.getPatientModel().getName());
            emailTv.setText(data.getPatientModel().getEmail());
            mobileTv.setText(data.getPatientModel().getMobile());
            addressTv.setText(data.getPatientModel().getAddress());


            Glide.with(context)
                    .load(data.getImgurl())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);

        }

        @Override
        public void onClick(View v) {
            if (medicineRequestItemClickListener != null) {
                medicineRequestItemClickListener.onMedicineRequestItemClick(medicineRequestModel);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout_medicine, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        MedicineRequestModel medicineRequestModel = medicineRequestModelArrayList.get(i);
        myViewHolder.setData(medicineRequestModel);
    }


    @Override
    public int getItemCount() {
        return medicineRequestModelArrayList.size();
    }

}
