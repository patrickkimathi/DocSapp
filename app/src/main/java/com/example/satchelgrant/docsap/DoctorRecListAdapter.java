package com.example.satchelgrant.docsap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.satchelgrant.docsap.models.Doctor;
import com.example.satchelgrant.docsap.ui.DoctorDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by satchelgrant on 12/2/16.
 */

public class DoctorRecListAdapter extends RecyclerView.Adapter<DoctorRecListAdapter.DoctorViewHolder> {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    private ArrayList<Doctor> mDoctors;
    private Context mContext;

    public DoctorRecListAdapter(Context context, ArrayList<Doctor> doctors) {
        mContext = context;
        mDoctors = doctors;
    }

    @Override
    public DoctorRecListAdapter.DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_list_item, parent, false);
        DoctorViewHolder viewHolder = new DoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DoctorRecListAdapter.DoctorViewHolder holder, int position) {
        holder.bindDoctors(mDoctors.get(position));
    }

    @Override
    public int getItemCount() {
        return mDoctors.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.doctorImageHolder) ImageView mDoctorImageView;
        @Bind(R.id.doctorNameHolder) TextView mDoctorNameView;
        @Bind(R.id.speciatiesTextView) TextView mDoctorSpecialtyView;
        @Bind(R.id.ratingTextView) TextView mDoctorRatingView;

        private Context mContext;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, DoctorDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("doctors", Parcels.wrap(mDoctors));
            mContext.startActivity(intent);
        }

        public void bindDoctors(Doctor doctor) {
            mDoctorNameView.setText(doctor.getFullNameWithTitle());
            mDoctorSpecialtyView.setText(android.text.TextUtils.join(", ", doctor.getSpecialties()));
            mDoctorRatingView.setText(doctor.getBetterDoctorRating());
            Picasso.with(mContext)
                    .load(doctor.getImageUrl())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(mDoctorImageView);
        }

    }

}
