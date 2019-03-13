package com.example.ldrp.medeeasyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ldrp.medeeasyapp.adapter.PresciptionListAdapter;
import com.example.ldrp.medeeasyapp.adapter.ReportListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.listener.PrescriptionItemClickListener;
import com.example.ldrp.medeeasyapp.model.PatientReviewModel;
import com.example.ldrp.medeeasyapp.model.PrescriptionModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class PatientPrescriptionViewActivity extends AppCompatActivity implements PrescriptionItemClickListener {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private PresciptionListAdapter presciptionListAdapter;
    private ArrayList<PrescriptionModel> prescriptionModelArrayList;
    private ProgressDialog progressDialog;
    private Intent intent;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription_view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(PatientPrescriptionViewActivity.this);
        intent = getIntent();
        uuid = intent.getStringExtra(AppConfig.KEY_DOCTOR_UID);

        recyclerView = findViewById(R.id.activity_patient_prescription_view_rv);

        prescriptionModelArrayList = new ArrayList<>();
        presciptionListAdapter = new PresciptionListAdapter(PatientPrescriptionViewActivity.this, prescriptionModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(presciptionListAdapter);
        recyclerView.setLayoutManager(layoutManager);



        databaseReference.child(AppConfig.FIREBASE_DB_APPOINMENT)
                .child(uuid)
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(AppConfig.FIREBASE_DB_PRESCRIPTION)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        prescriptionModelArrayList.clear();
                        for (DataSnapshot prescriptionModelSnapshot : dataSnapshot.getChildren()) {
                            PrescriptionModel prescriptionModel= prescriptionModelSnapshot.getValue(PrescriptionModel.class);
                            prescriptionModelArrayList.add(prescriptionModel);
                        }
                        Collections.reverse(prescriptionModelArrayList);
                        presciptionListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onPrescriptionItemClick(PrescriptionModel prescriptionModel) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(prescriptionModel.getImgUrl()));
        startActivity(intent);
    }
}
