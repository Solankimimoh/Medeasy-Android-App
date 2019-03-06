package com.example.ldrp.medeeasyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ldrp.medeeasyapp.adapter.ReminderListAdapter;
import com.example.ldrp.medeeasyapp.listener.ReminderItemClickListener;
import com.example.ldrp.medeeasyapp.model.ReminderModel;

import java.util.ArrayList;

public class BasicDiseasesListActivity extends AppCompatActivity implements ReminderItemClickListener {

    private ArrayList<ReminderModel> reminderModelArrayList;
    private RecyclerView recyclerView;
    private ReminderListAdapter reminderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_diseases_list);


        recyclerView = findViewById(R.id.activity_basic_diseases_rv);

        reminderModelArrayList = new ArrayList<>();
        reminderModelArrayList.add(new ReminderModel("Anxiety", "Everyone has feelings of anxiety at some point in their life. For example, you may feel worried and anxious about sitting an exam or having a medical test or job interview. During times like these, feeling anxious can be perfectly normal.", "Generalised anxiety disorder (GAD) can affect you both physically and mentally. How severe the symptoms are varies from person to person. Some people have only one or two symptoms, while others have many more. You should see your GP if anxiety is affecting your daily life or is causing you distress.", "See your GP if anxiety is affecting your daily life or is causing you distress. Generalised anxiety disorder(GAD)can be difficult to diagnose.In some cases, it can also be difficult to distinguish from other mental health conditions, such as depression."));
        reminderListAdapter = new ReminderListAdapter(BasicDiseasesListActivity.this, reminderModelArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(reminderListAdapter);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onReminderItemClick(ReminderModel reminderModel) {

    }
}
