package com.example.ldrp.medeeasyapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ldrp.medeeasyapp.adapter.DoctorListAdapter;
import com.example.ldrp.medeeasyapp.adapter.ReminderListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.listener.DoctorItemClickListener;
import com.example.ldrp.medeeasyapp.model.DoctorModel;
import com.example.ldrp.medeeasyapp.model.ReminderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorSelectPrescriptionActivity extends AppCompatActivity implements DoctorItemClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private DoctorListAdapter doctorListAdapter;
    private ArrayList<DoctorModel> doctorModelArrayList;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_select_prescription);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(DoctorSelectPrescriptionActivity.this);


        recyclerView = findViewById(R.id.activity_doctor_select_prescription_rv);

        doctorModelArrayList = new ArrayList<>();
        doctorListAdapter = new DoctorListAdapter(DoctorSelectPrescriptionActivity.this,
                doctorModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(doctorListAdapter);
        recyclerView.setLayoutManager(layoutManager);


        databaseReference
                .child(AppConfig.FIREBASE_DB_APPOINMENT)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        doctorModelArrayList.clear();
                        for (DataSnapshot doctorModelSnapshot : dataSnapshot.getChildren()) {

                            for (DataSnapshot patientDataSnapshot : doctorModelSnapshot.getChildren()) {

                                if (patientDataSnapshot.getKey().equals(firebaseAuth.getCurrentUser().getUid())) {

                                    databaseReference.child(AppConfig.FIREBASE_DB_DOCTOR)
                                            .child(doctorModelSnapshot.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Log.e("OK", dataSnapshot.getValue() + "");
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                }

                            }
                            doctorListAdapter.notifyDataSetChanged();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onDoctorItemClick(DoctorModel doctorModel) {

    }
}
