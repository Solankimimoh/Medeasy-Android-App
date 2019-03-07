package com.example.laboratory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPatientListActivity extends AppCompatActivity implements AppoinmentItemClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AppoinmentAdapter appoinmentAdapter;
    private ArrayList<LaboratoryAppoinmentModel> appoinmentModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_appoinment);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        recyclerView = findViewById(R.id.activity_view_request_appoinment);

        appoinmentModelArrayList = new ArrayList<>();
        appoinmentAdapter = new AppoinmentAdapter(ViewPatientListActivity.this, appoinmentModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(appoinmentAdapter);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference.child(AppConfig.FIREBASE_DB_LABORATORY_APPOINMENT)
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appoinmentModelArrayList.clear();

                        for (final DataSnapshot appoinmentModelSnapshot : dataSnapshot.getChildren()) {

                            for (DataSnapshot appoinmentModelSnapshotPush : appoinmentModelSnapshot.getChildren()) {

                                final LaboratoryAppoinmentModel appoinmentModel = appoinmentModelSnapshotPush.getValue(LaboratoryAppoinmentModel.class);
                                Log.e("MODEL", appoinmentModelSnapshotPush + "");
                                Log.e("MODEL", appoinmentModelSnapshot.getKey() + "");
                                if (appoinmentModel.isStatus()) {
                                    databaseReference.child(AppConfig.FIREBASE_DB_PATIENT)
                                            .child(appoinmentModelSnapshot.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    PatientModel patientModel = dataSnapshot.getValue(PatientModel.class);
                                                    patientModel.setUuid(dataSnapshot.getKey());
                                                    appoinmentModel.setPatientModel(patientModel);
                                                    Log.e("YES", dataSnapshot.getValue()
                                                            + appoinmentModel.getPatientModel().getAddress() + "");
                                                    appoinmentModelArrayList.add(appoinmentModel);
                                                    appoinmentAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }
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
    public void onAppoinmentItemClick(final LaboratoryAppoinmentModel appoinmentModel, View view) {


        PopupMenu popup = new PopupMenu(ViewPatientListActivity.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_add_report:
                        final Intent gotoReportView = new Intent(ViewPatientListActivity.this,
                                AddReportActivity.class);
                        gotoReportView.putExtra(AppConfig.KEY_PATIENT_UID, appoinmentModel.getPatientModel().getUuid());

                        startActivity(gotoReportView);
                        break;
                }
                return true;
            }
        });


    }

}
