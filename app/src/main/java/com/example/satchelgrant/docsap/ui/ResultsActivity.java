package com.example.satchelgrant.docsap.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.satchelgrant.docsap.DoctorRecListAdapter;
import com.example.satchelgrant.docsap.R;
import com.example.satchelgrant.docsap.models.Doctor;
import com.example.satchelgrant.docsap.services.DoctorService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String Tag = ResultsActivity.class.getSimpleName();
    private String mName;
    private String mQuery;
    private String mSpecialty;
    private ArrayList<Doctor> mDoctors;
    private DoctorRecListAdapter mAdapter;

    @Bind(R.id.doctorRecycler) RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);

        Typeface droidSans = Typeface.createFromAsset(getAssets(), "fonts/DroidSans.ttf");

        Intent intent = getIntent();
        mQuery = intent.getStringExtra("query");
        mName = intent.getStringExtra("name");
        mSpecialty = intent.getStringExtra("specialty");

        findDoctors(mQuery, mName, mSpecialty);

    }

    public void findDoctors(String query, String name, String specialty) {
        final DoctorService service = new DoctorService();
        service.getDoctors(query, name, specialty, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mDoctors = service.processResponse(response);

                ResultsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new DoctorRecListAdapter(getApplicationContext(), mDoctors);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager=
                                new LinearLayoutManager(ResultsActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}