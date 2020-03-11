package com.barmeg.travelerjourney;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JourneysListAdapter extends RecyclerView.Adapter<JourneysListAdapter.JourneyViewHolder> {
    public interface OnJourneyClickListener {

        void OnJourneyClick(Journey journey);
    }

    private List<Journey> mJourneysList;

    private OnJourneyClickListener mOnJourneyClickListener;

    public JourneysListAdapter(List<Journey> mJourneysList, OnJourneyClickListener onJourneyClickListener){
        this.mJourneysList = mJourneysList;
        this.mOnJourneyClickListener =  onJourneyClickListener;
    }
    @NonNull
    @Override
    public JourneysListAdapter.JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()  ).inflate( R.layout.item_journey, parent, false );
        return new JourneyViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull JourneysListAdapter.JourneyViewHolder holder, int position) {
        holder.bind( mJourneysList.get( position ));

    }

    @Override
    public int getItemCount() {
        return mJourneysList.size();
    }

    public class JourneyViewHolder extends RecyclerView.ViewHolder {
        TextView journyTitleTextView;
        TextView journeyDateTextView;
        ImageView journeyPhotoImageView;
        Journey journey;
        public JourneyViewHolder(@NonNull View itemView) {
            super( itemView );
            journyTitleTextView = itemView.findViewById( R.id.text_view_journey_title );
            journeyDateTextView = itemView.findViewById( R.id.text_view_journey_date );
            journeyPhotoImageView = itemView.findViewById( R.id.image_view_journey_photo );
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnJourneyClickListener.OnJourneyClick( journey );

                }
            } );
        }

        public void bind(Journey journey){
            this.journey = journey;
            journyTitleTextView.setText(journey.getTitle());
            journeyDateTextView.setText( journey.getFormatedDate());
            Glide.with(journeyPhotoImageView).load( journey.getPhoto()).into( journeyPhotoImageView);
        }
    }
}
