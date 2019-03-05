package com.example.ldrp.medeeasyapp;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ldrp.medeeasyapp.adapter.AppoinmentAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.doctor.DoctorHomeActivity;
import com.example.ldrp.medeeasyapp.listener.AppoinmentItemClickListener;
import com.example.ldrp.medeeasyapp.model.AppoinmentModel;
import com.example.ldrp.medeeasyapp.model.PatientModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewRequestAppoinmentActivity extends AppCompatActivity implements AppoinmentItemClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AppoinmentAdapter appoinmentAdapter;
    private ArrayList<AppoinmentModel> appoinmentModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_appoinment);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        recyclerView = findViewById(R.id.activity_view_request_appoinment);

        appoinmentModelArrayList = new ArrayList<>();
        appoinmentAdapter = new AppoinmentAdapter(ViewRequestAppoinmentActivity.this, appoinmentModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(appoinmentAdapter);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference.child(AppConfig.FIREBASE_DB_APPOINMENT)
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appoinmentModelArrayList.clear();

                        for (DataSnapshot appoinmentModelSnapshot : dataSnapshot.getChildren()) {
                            final AppoinmentModel appoinmentModel = appoinmentModelSnapshot.getValue(AppoinmentModel.class);
                            Log.e("MODEL", appoinmentModelSnapshot.getRef() + "");
                            Log.e("MODEL", appoinmentModelSnapshot.getKey() + "");

                            if (!appoinmentModel.isStatus()) {
                                databaseReference.child(AppConfig.FIREBASE_DB_PATIENT)
                                        .child(appoinmentModelSnapshot.getKey())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                PatientModel patientModel = dataSnapshot.getValue(PatientModel.class);
                                                patientModel.setUuid(dataSnapshot.getKey());
                                                appoinmentModel.setPatientModel(patientModel);
                                                Log.e("MODEL", appoinmentModel.getRemarks() + appoinmentModel.getPatientModel().getAddress() + "");
                                                appoinmentModelArrayList.add(appoinmentModel);
                                                appoinmentAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        }
                        appoinmentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    @Override
    public void onAppoinmentItemClick(final AppoinmentModel appoinmentModel) {


        AlertDialog.Builder builder = new AlertDialog.Builder(ViewRequestAppoinmentActivity.this);


        builder.setTitle("Booking Confirmation");
        builder.setMessage("Are you sure want to Accept");


        builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(AppConfig.FIREBASE_DB_APPOINMENT)
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                appoinmentModelArrayList.clear();
                                for (DataSnapshot appoinmentModelSnapshot : dataSnapshot.getChildren()) {

                                    AppoinmentModel appoinmentModel1 = appoinmentModelSnapshot.getValue(AppoinmentModel.class);


                                    if (appoinmentModelSnapshot.getKey().equals(appoinmentModel.getPatientModel().getUuid())) {

                                        appoinmentModelSnapshot.getRef()
                                                .child("status").setValue(true);

                                    }

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });


        builder.setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();



    }
}
