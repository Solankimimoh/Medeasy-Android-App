package com.example.ldrp.medeeasyapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ldrp.medeeasyapp.adapter.AppoinmentAdapter;
import com.example.ldrp.medeeasyapp.adapter.DoctorListAdapter;
import com.example.ldrp.medeeasyapp.adapter.MedicalListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.doctor.DoctorHomeActivity;
import com.example.ldrp.medeeasyapp.listener.AppoinmentItemClickListener;
import com.example.ldrp.medeeasyapp.listener.MedicalListtemClickListener;
import com.example.ldrp.medeeasyapp.model.AppoinmentModel;
import com.example.ldrp.medeeasyapp.model.DoctorModel;
import com.example.ldrp.medeeasyapp.model.MedicalModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedicalListActivity extends AppCompatActivity implements MedicalListtemClickListener {

    private ArrayList<MedicalModel> medicalModelArrayList;
    private MedicalListAdapter medicalListAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_list);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        recyclerView = findViewById(R.id.activity_medical_rv);

        medicalModelArrayList = new ArrayList<>();
        medicalListAdapter = new MedicalListAdapter(MedicalListActivity.this, medicalModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(medicalListAdapter);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference.child(AppConfig.FIREBASE_DB_MEDICAL)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot medicalModelSnapshot : dataSnapshot.getChildren()) {
                            MedicalModel medicalModel = medicalModelSnapshot.getValue(MedicalModel.class);
                            medicalModel.setUid(medicalModelSnapshot.getKey());
                            medicalModelArrayList.add(medicalModel);
                        }
                        medicalListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onMedicalListtemClick(MedicalModel medicalModel, View view) {

    }

}
