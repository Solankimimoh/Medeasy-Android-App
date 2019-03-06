package com.example.ldrp.medeeasyapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.adapter.AppoinmentAdapter;
import com.example.ldrp.medeeasyapp.adapter.PresciptionListAdapter;
import com.example.ldrp.medeeasyapp.adapter.ReportListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.doctor.DoctorHomeActivity;
import com.example.ldrp.medeeasyapp.listener.AppoinmentItemClickListener;
import com.example.ldrp.medeeasyapp.listener.ReportItemClickListener;
import com.example.ldrp.medeeasyapp.model.AppoinmentModel;
import com.example.ldrp.medeeasyapp.model.PatientReviewModel;
import com.example.ldrp.medeeasyapp.model.PrescriptionModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener, ReportItemClickListener {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ReportListAdapter reportListAdapter;
    private ArrayList<PatientReviewModel> patientReviewModelArrayList;

    private EditText titleEd;
    private EditText descriptionEd;
    private Button addBtn;
    private String uuid;
    private Intent intent;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        initView();
        progressDialog = new ProgressDialog(ReportActivity.this);
        intent = getIntent();
        uuid = intent.getStringExtra(AppConfig.KEY_PATIENT_UID);

        recyclerView = findViewById(R.id.activity_report_list_rv);

        patientReviewModelArrayList = new ArrayList<>();
        reportListAdapter = new ReportListAdapter(ReportActivity.this, patientReviewModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(layoutManager);


        databaseReference.child(AppConfig.FIREBASE_DB_APPOINMENT)
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(uuid)
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

    private void initView() {
        titleEd = findViewById(R.id.activity_report_title_ed);
        descriptionEd = findViewById(R.id.activity_report_description_ed);
        addBtn = findViewById(R.id.activity_report_add_btn);

        addBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        progressDialog.setTitle("Report Insert");
        progressDialog.setMessage("Report Insert......");
        progressDialog.show();

        final String title = titleEd.getText().toString().trim();
        final String description = descriptionEd.getText().toString().trim();

        if (!title.isEmpty() || !description.isEmpty()) {
            databaseReference.child(AppConfig.FIREBASE_DB_APPOINMENT)
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .child(uuid)
                    .child(AppConfig.FIREBASE_DB_REPORT)
                    .push()
                    .setValue(new PatientReviewModel(title, description), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(ReportActivity.this, "Error " +
                                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(ReportActivity.this, "Report Add", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }else
        {
            Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onReportItemClick(PatientReviewModel patientReviewModel) {

    }
}
