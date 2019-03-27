package com.example.ldrp.medeeasyapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.adapter.LabReportListAdapter;
import com.example.ldrp.medeeasyapp.adapter.MedicalListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.listener.LabReportItemClickListener;
import com.example.ldrp.medeeasyapp.listener.MedicalListtemClickListener;
import com.example.ldrp.medeeasyapp.model.LabReportModel;
import com.example.ldrp.medeeasyapp.model.MedicalModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientViewLabReportActivity extends AppCompatActivity implements LabReportItemClickListener {


    private ArrayList<LabReportModel> labReportModelArrayList;
    private LabReportListAdapter labReportListAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_lab_report);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        intent = getIntent();

        recyclerView = findViewById(R.id.activity_patient_view_lab_report_rv);

        labReportModelArrayList = new ArrayList<>();
        labReportListAdapter = new LabReportListAdapter(PatientViewLabReportActivity.this, labReportModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(labReportListAdapter);
        recyclerView.setLayoutManager(layoutManager);


        if (intent.hasExtra(AppConfig.KEY_PATIENT_UID)) {
            final String uuid = intent.getStringExtra(AppConfig.KEY_PATIENT_UID);
            getLabReport(uuid);
        } else {
            final String uuid = firebaseAuth.getCurrentUser().getUid();
            getLabReport(uuid);
        }


    }

    private void getLabReport(final String uuid) {



        databaseReference
                .child(AppConfig.FIREBASE_DB_LABORATORY_APPOINMENT)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot labPushSnapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot patientPushSnapshot : labPushSnapshot.getChildren()) {
                                if (patientPushSnapshot.getKey().equals(uuid)) {
                                    for (DataSnapshot reportSnapshot : patientPushSnapshot.child("report").getChildren()) {
                                        LabReportModel labReportModel = reportSnapshot.getValue(LabReportModel.class);
                                        labReportModelArrayList.add(labReportModel);
                                    }
                                    labReportListAdapter.notifyDataSetChanged();
                                }
                                labReportListAdapter.notifyDataSetChanged();
                            }
                            labReportListAdapter.notifyDataSetChanged();
                        }
                        labReportListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onLabReportItemClick(LabReportModel labReportModel) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(labReportModel.getFileUrl()));
        startActivity(intent);
    }
}
