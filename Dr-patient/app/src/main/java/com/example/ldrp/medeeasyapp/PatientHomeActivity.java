package com.example.ldrp.medeeasyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.adapter.DoctorListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.listener.DoctorItemClickListener;
import com.example.ldrp.medeeasyapp.model.DoctorModel;
import com.example.ldrp.medeeasyapp.patient.BookAppoinmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DoctorItemClickListener {

    private ArrayList<DoctorModel> doctorModelArrayList;
    private DoctorListAdapter doctorListAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        initView();
        setupNavigationDrawer();


        doctorModelArrayList = new ArrayList<>();
        doctorListAdapter = new DoctorListAdapter(PatientHomeActivity.this,
                doctorModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(doctorListAdapter);
        recyclerView.setLayoutManager(layoutManager);


        databaseReference.child(AppConfig.FIREBASE_DB_DOCTOR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot doctorModelSnapshot : dataSnapshot.getChildren()) {
                    DoctorModel doctorModel = doctorModelSnapshot.getValue(DoctorModel.class);
                    doctorModel.setUid(doctorModelSnapshot.getKey());
                    doctorModelArrayList.add(doctorModel);
                }
                doctorListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setupNavigationDrawer() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {
        recyclerView = findViewById(R.id.activity_patient_home_doctor_rv);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_patient_home) {
            // Handle the camera action
        } else if (id == R.id.nav_patient_book_prescription) {
            final Intent gotoDoctorSelect = new Intent(PatientHomeActivity.this, DoctorSelectPrescriptionActivity.class);
            startActivity(gotoDoctorSelect);
        } else if (id == R.id.nav_patient_lab_report) {

        } else if (id == R.id.nav_patient_medical) {

        } else if (id == R.id.nav_patient_laboratory) {

        } else if (id == R.id.nav_patient_quick_medicine) {
            final Intent gotoBasicList = new Intent(PatientHomeActivity.this, BasicDiseasesListActivity.class);
            startActivity(gotoBasicList);

        } else if (id == R.id.nav_patient_logout) {

        } else if (id == R.id.nav_patient_book_reminder) {
            final Intent gotoReminder = new Intent(PatientHomeActivity.this, PatientReminderActivity.class);
            startActivity(gotoReminder);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDoctorItemClick(DoctorModel doctorModel,View view) {

        final Intent gotoBookAppointment = new Intent(PatientHomeActivity.this,
                BookAppoinmentActivity.class);
        gotoBookAppointment.putExtra(AppConfig.KEY_DOCTOR_UID, doctorModel.getUid());
        startActivity(gotoBookAppointment);


    }
}
