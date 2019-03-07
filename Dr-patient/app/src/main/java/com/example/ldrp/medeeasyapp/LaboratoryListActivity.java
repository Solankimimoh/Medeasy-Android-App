package com.example.ldrp.medeeasyapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.ldrp.medeeasyapp.adapter.DoctorListAdapter;
import com.example.ldrp.medeeasyapp.adapter.LaboratoryListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.listener.LaboratoryItemClickListener;
import com.example.ldrp.medeeasyapp.model.DoctorModel;
import com.example.ldrp.medeeasyapp.model.LaboratoryModel;
import com.example.ldrp.medeeasyapp.patient.BookAppoinmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LaboratoryListActivity extends AppCompatActivity implements LaboratoryItemClickListener {

    private ArrayList<LaboratoryModel> laboratoryModelArrayList;
    private LaboratoryListAdapter laboratoryListAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory_list);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView = findViewById(R.id.activity_laboratory_list_rv);

        laboratoryModelArrayList = new ArrayList<>();
        laboratoryListAdapter = new LaboratoryListAdapter(LaboratoryListActivity.this,
                laboratoryModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(laboratoryListAdapter);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference.child(AppConfig.FIREBASE_DB_LABORATORY)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot laboratoryModelSnapshot : dataSnapshot.getChildren()) {
                            LaboratoryModel laboratoryModel = laboratoryModelSnapshot.getValue(LaboratoryModel.class);
                            laboratoryModel.setUid(laboratoryModelSnapshot.getKey());
                            laboratoryModelArrayList.add(laboratoryModel);
                            Log.e("OK", laboratoryModel.getName() + "");

                        }
                        laboratoryListAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    @Override
    public void onLaboratoryItemClickListener(LaboratoryModel laboratoryModel, View view) {
        final Intent gotoLaboratoryBookAppoinemnt = new Intent(LaboratoryListActivity.this,
                LaboratoryBookingActivity.class);
        gotoLaboratoryBookAppoinemnt.putExtra(AppConfig.KEY_LABORATORY_UID, laboratoryModel.getUid());
        startActivity(gotoLaboratoryBookAppoinemnt);
    }
}
