package com.example.ldrp.medeeasyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ldrp.medeeasyapp.adapter.ReportListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.listener.ReportItemClickListener;
import com.example.ldrp.medeeasyapp.model.PatientReviewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientReportViewActivity extends AppCompatActivity implements ReportItemClickListener {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ReportListAdapter reportListAdapter;
    private ArrayList<PatientReviewModel> patientReviewModelArrayList;
    private ProgressDialog progressDialog;
    private Intent intent;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_report_view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(PatientReportViewActivity.this);
        intent = getIntent();
        uuid = intent.getStringExtra(AppConfig.KEY_DOCTOR_UID);

        recyclerView = findViewById(R.id.activity_patient_report_view_rv);

        patientReviewModelArrayList = new ArrayList<>();
        reportListAdapter = new ReportListAdapter(PatientReportViewActivity.this, patientReviewModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(layoutManager);


        databaseReference.child(AppConfig.FIREBASE_DB_APPOINMENT)
                .child(uuid)
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(AppConfig.FIREBASE_DB_REPORT)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        patientReviewModelArrayList.clear();
                        for (DataSnapshot reviewModelSnapshot : dataSnapshot.getChildren()) {
                            PatientReviewModel patientReviewModel = reviewModelSnapshot.getValue(PatientReviewModel.class);
                            patientReviewModelArrayList.add(patientReviewModel);
                        }
                        reportListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onReportItemClick(PatientReviewModel patientReviewModel) {

    }
}
