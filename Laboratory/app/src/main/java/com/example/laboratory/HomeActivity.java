package com.example.laboratory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AppoinmentItemClickListener {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AppoinmentAdapter appoinmentAdapter;
    private ArrayList<LaboratoryAppoinmentModel> appoinmentModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        recyclerView = findViewById(R.id.content_home_appoinment);

        appoinmentModelArrayList = new ArrayList<>();
        appoinmentAdapter = new AppoinmentAdapter(HomeActivity.this, appoinmentModelArrayList, this);
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
                                if (!appoinmentModel.isStatus()) {
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


        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);


        builder.setTitle("Booking Confirmation");
        builder.setMessage("Are you sure want to Accept");


        builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(AppConfig.FIREBASE_DB_LABORATORY_APPOINMENT)
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                appoinmentModelArrayList.clear();
                                for (DataSnapshot appoinmentModelSnapshot : dataSnapshot.getChildren()) {

                                    for (DataSnapshot appoinmentModel : appoinmentModelSnapshot.getChildren()) {
                                        LaboratoryAppoinmentModel appoinmentModel1 = appoinmentModel.getValue(LaboratoryAppoinmentModel.class);

                                        appoinmentModel.getRef()
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
        getMenuInflater().inflate(R.menu.home, menu);
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
        } else if (id == R.id.nav_report) {

            final Intent gotoViewList = new Intent(HomeActivity.this, ViewPatientListActivity.class);
            startActivity(gotoViewList);
        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
