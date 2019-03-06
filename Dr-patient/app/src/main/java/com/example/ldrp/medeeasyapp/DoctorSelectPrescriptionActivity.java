package com.example.ldrp.medeeasyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.adapter.DoctorListAdapter;
import com.example.ldrp.medeeasyapp.adapter.ReminderListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.doctor.DoctorHomeActivity;
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
                                                    DoctorModel doctorModel = dataSnapshot.getValue(DoctorModel.class);
                                                    doctorModel.setUid(dataSnapshot.getKey());
                                                    doctorModelArrayList.add(doctorModel);
                                                    doctorListAdapter.notifyDataSetChanged();

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
    public void onDoctorItemClick(final DoctorModel doctorModel, View view) {
        PopupMenu popup = new PopupMenu(DoctorSelectPrescriptionActivity.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_report:

                        Toast.makeText(DoctorSelectPrescriptionActivity.this, "" + doctorModel.getUid(), Toast.LENGTH_SHORT).show();

                        final Intent gotoReportView = new Intent(DoctorSelectPrescriptionActivity.this,
                                PatientReportViewActivity.class);
                        gotoReportView.putExtra(AppConfig.KEY_DOCTOR_UID, doctorModel.getUid());

                        startActivity(gotoReportView);
                        break;
                    case R.id.popup_prescription:
                        final Intent gotoPrescriptionView = new Intent(DoctorSelectPrescriptionActivity.this,
                                PatientPrescriptionViewActivity.class);
                        gotoPrescriptionView.putExtra(AppConfig.KEY_DOCTOR_UID, doctorModel.getUid());
                        startActivity(gotoPrescriptionView);
                        break;
                }
                return true;
            }
        });

    }
}
