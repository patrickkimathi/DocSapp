package com.example.satchelgrant.docsap.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.satchelgrant.docsap.Constants;
import com.example.satchelgrant.docsap.R;
import com.example.satchelgrant.docsap.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewReviewActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.reviewDoctorBanner) TextView mBanner;
    @Bind(R.id.doctorRatingBar) RatingBar mRatingBar;
    @Bind(R.id.doctorReviewText) EditText mReview;
    @Bind(R.id.submitReviewButton) Button mSubmitReviewButton;

    private String mDocId;

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mSharedPrefsEditor;

    private DatabaseReference mReviewsReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);
        ButterKnife.bind(this);

        mReviewsReference = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_REVIEWS);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDocId = mSharedPrefs.getString("doctorId", "Error");

        Intent intent = getIntent();
        String docName = intent.getStringExtra("name");

        mBanner.setText("Review " + docName);

        mSubmitReviewButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mSubmitReviewButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            String userName = user.getDisplayName();
            String review = mReview.getText().toString();
            review.trim();
            float rating = mRatingBar.getRating();
            if(review.isEmpty()) {
                review = "No review provided";
            }
            Review newReview = new Review(uid, userName, review, rating);

            DatabaseReference docIdRef = mReviewsReference.child(mDocId);
            DatabaseReference pushRef = docIdRef.push();
            String pushId = pushRef.getKey();
            newReview.setPushId(pushId);

            pushRef.setValue(newReview);

            Intent newIntent = new Intent(NewReviewActivity.this, ReviewedDoctorsActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_home) {
            Intent intent = new Intent(NewReviewActivity.this, SplashActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}


