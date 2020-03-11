package com.barmeg.travelerjourney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class JourneyDetailsActivity extends AppCompatActivity {
    public static final String JOURNEY_DATA = "journey_data";

    private ImageView mJourneyImageView;
    private TextView mDateTextView;
    private TextView mDescriptionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_journey_details );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        mJourneyImageView = findViewById( R.id.image_view_journey_photo);
        mDateTextView = findViewById( R.id.text_view_journey_date);
        mDescriptionTextView = findViewById( R.id.text_view_journey_description);

        if(getIntent() != null && getIntent().getExtras() != null){
            Journey journey = getIntent().getExtras().getParcelable( JOURNEY_DATA );
            if(journey != null){
                getSupportActionBar().setTitle(journey.getTitle());
                mDateTextView.setText( journey.getFormatedDate() );
                mDescriptionTextView.setText( journey.getDescription());
                Glide.with( this).load(journey.getPhoto()  ).into( mJourneyImageView);

            }
        }
    }
}
