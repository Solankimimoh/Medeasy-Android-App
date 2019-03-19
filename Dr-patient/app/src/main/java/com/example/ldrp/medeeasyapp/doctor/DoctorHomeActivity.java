package com.example.ldrp.medeeasyapp.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.DoctorReminderActivity;
import com.example.ldrp.medeeasyapp.PresciptionActivity;
import com.example.ldrp.medeeasyapp.R;
import com.example.ldrp.medeeasyapp.ReportActivity;
import com.example.ldrp.medeeasyapp.ViewRequestAppoinmentActivity;
import com.example.ldrp.medeeasyapp.adapter.AppoinmentAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
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


public class DoctorHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AppoinmentItemClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AppoinmentAdapter appoinmentAdapter;
    private ArrayList<AppoinmentModel> appoinmentModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        setupNavigationDrawer(toolbar);

        recyclerView = findViewById(R.id.activity_doctor_home_appoinment_rv);

        appoinmentModelArrayList = new ArrayList<>();
        appoinmentAdapter = new AppoinmentAdapter(DoctorHomeActivity.this, appoinmentModelArrayList, this);
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

                            if (appoinmentModel.isStatus()) {
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

    private void setupNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);

        TextView textView = hView.findViewById(R.id.textView1);
        textView.setText(firebaseAuth.getCurrentUser().getEmail());

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
        getMenuInflater().inflate(R.menu.doctor_home, menu);
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

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_appoinment) {
            final Intent gotoViewRequestAppoinmentActivity = new Intent(DoctorHomeActivity.this,
                    ViewRequestAppoinmentActivity.class);
            startActivity(gotoViewRequestAppoinmentActivity);
        } else if (id == R.id.nav_reminder) {
            final Intent gotoDoctorReminder = new Intent(DoctorHomeActivity.this,
                    DoctorReminderActivity.class);
            startActivity(gotoDoctorReminder);
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAppoinmentItemClick(final AppoinmentModel appoinmentModel, View view) {


        PopupMenu popup = new PopupMenu(DoctorHomeActivity.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_report:
                        final Intent gotoReportView = new Intent(DoctorHomeActivity.this,
                                ReportActivity.class);
                        gotoReportView.putExtra(AppConfig.KEY_PATIENT_UID, appoinmentModel.getPatientModel().getUuid());

                        startActivity(gotoReportView);
                        break;
                    case R.id.popup_prescription:
                        final Intent gotoPrescriptionView = new Intent(DoctorHomeActivity.this,
                                PresciptionActivity.class);
                        gotoPrescriptionView.putExtra(AppConfig.KEY_PATIENT_UID, appoinmentModel.getPatientModel().getUuid());
                        startActivity(gotoPrescriptionView);
                        break;
                }
                return true;
            }
        });


    }
}
