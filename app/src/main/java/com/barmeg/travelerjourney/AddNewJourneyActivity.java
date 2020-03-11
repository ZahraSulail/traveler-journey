package com.barmeg.travelerjourney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewJourneyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ConstraintLayout mConstraintLayout;
    private TextInputLayout mJourneyTitleTextInputLayout;
    private TextInputLayout mJourneyDescriptionTextInputLayout;
    private TextInputLayout mJourneyDateTextInputLayout;
    private TextInputEditText mJourneyTitleTextinputEditText;
    private TextInputEditText mJourneyDescriptionTextinputEditText;
    private TextInputEditText mJourneyDateTextinputEditText;
    private ImageView mJourneyImageView;
    private Button mChoosePhotoButton;
    private Button mAddJourneyButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_journey );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );

        mConstraintLayout = findViewById( R.id.add_journey_coord_layout );
        mJourneyTitleTextInputLayout = findViewById( R.id.input_layout_title );
        mJourneyDateTextInputLayout = findViewById( R.id.input_layout_date );
        mJourneyDescriptionTextInputLayout = findViewById( R.id.input_layout_description );
        mJourneyTitleTextinputEditText = findViewById( R.id.edit_text_journey_title );
        mJourneyDateTextinputEditText = findViewById( R.id.edit_text_journey_date );
        mJourneyDescriptionTextinputEditText = findViewById( R.id.edit_text_journey_desc );
        mJourneyImageView = findViewById( R.id.image_view_journey );
        mChoosePhotoButton = findViewById( R.id.button_choose_photo );
        mAddJourneyButton = findViewById( R.id.button_add_journey );


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
