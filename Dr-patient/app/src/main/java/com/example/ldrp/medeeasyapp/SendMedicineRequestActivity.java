package com.example.ldrp.medeeasyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ldrp.medeeasyapp.adapter.PresciptionListAdapter;
import com.example.ldrp.medeeasyapp.app.AppConfig;
import com.example.ldrp.medeeasyapp.model.MedicineRequestModel;
import com.example.ldrp.medeeasyapp.model.PatientModel;
import com.example.ldrp.medeeasyapp.model.PrescriptionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class SendMedicineRequestActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int PICK_IMG_CODE = 200;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;

    private EditText titleEd;
    private EditText descriptionEd;
    private Button imageChooseBtn;
    private Button uploadBtn;
    private ProgressDialog progressDialog;


    private Intent intent;
    private Uri selectedFileIntent;
    private String uuid;
    private PatientModel patientModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_medicine_request);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        intent = getIntent();
        progressDialog = new ProgressDialog(SendMedicineRequestActivity.this);
        initView();
        uuid = intent.getStringExtra(AppConfig.KEY_MEDICAL_UID);

        databaseReference.child(AppConfig.FIREBASE_DB_PATIENT)
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        patientModel = dataSnapshot.getValue(PatientModel.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void initView() {
        titleEd = findViewById(R.id.activity_send_medicine_request_upload_title);
        descriptionEd = findViewById(R.id.activity_send_medicine_request_upload_description);
        imageChooseBtn = findViewById(R.id.activity_send_medicine_request_upload_image_choose);
        uploadBtn = findViewById(R.id.activity_send_medicine_request_upload);

        imageChooseBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_send_medicine_request_upload_image_choose:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                } //creating an intent for file chooser
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMG_CODE);

                break;
            case R.id.activity_send_medicine_request_upload:
                Toast.makeText(this, "sadasd", Toast.LENGTH_SHORT).show();
                uploadImage();
                break;
        }
    }

    private void uploadImage() {

        if (selectedFileIntent != null) {
            progressDialog.setTitle("Prescription Upload");
            progressDialog.setMessage("file uploading....");
            progressDialog.show();
            final StorageReference sRef = storageReference.child("medicine/" + UUID.randomUUID());

            sRef.putFile(selectedFileIntent)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    insertDataIntoDatabase(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        } else {
            Toast.makeText(this, "Not Image Choose", Toast.LENGTH_SHORT).show();
        }

    }

    private void insertDataIntoDatabase(String url) {

        progressDialog.setMessage("Data inserting...");

        final String title = titleEd.getText().toString().trim();
        final String description = descriptionEd.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Not Filled the Data", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            databaseReference.child(AppConfig.FIREBASE_DB_REQUEST_MEDICINE)
                    .child(uuid)
                    .push()
                    .setValue(new MedicineRequestModel(title, description, url,patientModel), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(SendMedicineRequestActivity.this, "Error " +
                                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(SendMedicineRequestActivity.this, "Prescirption upload", Toast.LENGTH_SHORT).show();
                                finish();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_IMG_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                File file = new File(data.getData().getPathSegments().toString());
                selectedFileIntent = data.getData();

                imageChooseBtn.setText(file.getName().substring(0, file.getName().length() - 1));
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
