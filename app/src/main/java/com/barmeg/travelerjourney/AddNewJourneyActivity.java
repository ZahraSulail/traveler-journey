package com.barmeg.travelerjourney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddNewJourneyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = AddNewJourneyActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_ACCESS_LOCATION = 1;
    private static final int PERMISSION_REQUEST_READ_STORAGE = 2;
    private static final int REQUEST_GET_PHOTO = 3;
    private static final LatLng DEFAULT_LOCATION = new LatLng( 29.3760641, 47.9643571 );
    private Uri mJourneyUri;

    private FusedLocationProviderClient mLocationProviderClient;
    private Location mLastKnownLocation;
    private LatLng mSelectedLatLng;
    private GoogleMap mGoogleMap;
    private Date mJourneyDate;
    private boolean mLocationPermissionGranted;
    private boolean mReadStoragePermissionGranted;
    private ProgressDialog mDialog;

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

        mJourneyDateTextinputEditText.setInputType( InputType.TYPE_NULL );

        mJourneyDateTextinputEditText.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
              if(hasFocus){
                  showDateChooserDialog();
              }else{
               view.clearFocus();
              }
            }
        } );

        requestLocationPermission();
        requestExternalStoragePermission();

        mChoosePhotoButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanchGalleryIntent();
            }
        } );

        mAddJourneyButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJourneyTitleTextInputLayout.setError( null );
                mJourneyDescriptionTextInputLayout.setError( null );
                mJourneyDateTextInputLayout.setError( null );
                if(TextUtils.isEmpty( mJourneyTitleTextinputEditText.getText() )){
                    mJourneyDateTextInputLayout.setError( getString( R.string.error_msg_title ) );
                }else{

                    if(TextUtils.isEmpty( mJourneyDescriptionTextinputEditText.getText() )){
                        mJourneyDescriptionTextInputLayout.setError( getString( R.string.error_msg_description ) );
                    }else{

                        if(TextUtils.isEmpty( mJourneyDateTextinputEditText.getText() )){
                            mJourneyDateTextInputLayout.setError( getString( R.string.error_msg_date ) );
                        }else{
                            if(mJourneyUri != null){
                                addJourneyToFirebase();

                            }
                        }

                    }
                }
            }
        } );

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient( this );

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if(mLocationPermissionGranted){

            requestDeviceCurrentLocation();
        }
        mGoogleMap.setOnMapClickListener( new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mSelectedLatLng = latLng;
                mGoogleMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position( mSelectedLatLng );
                markerOptions.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mGoogleMap.addMarker( markerOptions);
            }
        } );

    }
    private void addJourneyToFirebase(){
     FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
     StorageReference storageReference = firebaseStorage.getReference();
     final StorageReference photoStorageReference = storageReference.child( UUID.randomUUID().toString());
     final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
     mDialog = new ProgressDialog( this );
     mDialog.setIndeterminate( true );
     mDialog.setTitle( R.string.app_name );
     mDialog.setMessage( getString( R.string.uploading_photo ) );
     mDialog.show();
     photoStorageReference.putFile( mJourneyUri ).addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
             if(task.isSuccessful()){
                 photoStorageReference.getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {
                     @Override
                     public void onComplete(@NonNull Task<Uri> task) {
                         if(task.isSuccessful()){
                             final Journey journey = new Journey();
                             journey.setTitle( mJourneyTitleTextinputEditText.getText().toString());
                             journey.setDescription( mJourneyDescriptionTextinputEditText.getText().toString());
                             journey.setDate( new Timestamp( mJourneyDate));
                             journey.setPhoto( task.toString());
                             journey.setLocation( new GeoPoint( mSelectedLatLng.latitude, mSelectedLatLng.longitude));
                             firebaseFirestore.collection( "journeys" ).add(journey).addOnCompleteListener( new OnCompleteListener<DocumentReference>() {
                                 @Override
                                 public void onComplete(@NonNull Task<DocumentReference> task) {
                                     if(task.isSuccessful()){
                                         Snackbar.make( mConstraintLayout, R.string.add_journey_success, Snackbar.LENGTH_SHORT ).addCallback(new Snackbar.Callback(){
                                             @Override
                                             public void onDismissed(Snackbar transientBottomBar, int event) {
                                                 super.onDismissed( transientBottomBar, event );
                                                 mDialog.dismiss();
                                                 finish();
                                             }
                                         } ).show();
                                     }else{
                                         Snackbar.make( mConstraintLayout, R.string.add_journey_failed, Snackbar.LENGTH_LONG ).show();
                                         mDialog.dismiss();
                                     }
                                 }
                             } );
                         }else {
                             Snackbar.make( mConstraintLayout, R.string.uploaded_task_failed, Snackbar.LENGTH_LONG ).show();
                             mDialog.dismiss();

                         }
                     }
                 } );
             }else {
                 Snackbar.make( mConstraintLayout, R.string.add_journey_failed, Snackbar.LENGTH_LONG );
                 mDialog.dismiss();

             }
         }
     } );

    }
    private void showDateChooserDialog(){
        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMounth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
             Calendar newCalendr = Calendar.getInstance();
             newCalendr.set(year,month,dayOfMonth);
             mJourneyDate = newCalendr.getTime();
             DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
             mJourneyDateTextinputEditText.setText(dateFormat.format(newCalendr.getTime()));

            }
        } , startYear, startMounth, startDay);
         datePickerDialog.show();
    }

    private void requestLocationPermission(){
        mLocationPermissionGranted = false;
        if(ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){
        mLocationPermissionGranted = true;

        }else {

            ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION );
        }
    }
    private void requestExternalStoragePermission(){
        mReadStoragePermissionGranted = false;
        if(ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
            mReadStoragePermissionGranted = true;
        }else{
            ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_STORAGE );
        }
    }
    private void requestDeviceCurrentLocation(){

        Task<Location> locationResult = mLocationProviderClient.getLastLocation();
        locationResult.addOnSuccessListener( new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
             if(location != null){
                 mLastKnownLocation = location;
                 mSelectedLatLng = new LatLng( mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude() );
                 mGoogleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( mSelectedLatLng , 15 ));
                 MarkerOptions markerOptions = new MarkerOptions();
                 markerOptions.position( mSelectedLatLng );
                 markerOptions.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

             }else{

                 mGoogleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( DEFAULT_LOCATION, 15 ) );
             }
            }
        } );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode) {
           case PERMISSION_REQUEST_ACCESS_LOCATION:
               mLocationPermissionGranted = false;
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   mLocationPermissionGranted = true;
                   requestDeviceCurrentLocation();
               }
               break;
           case PERMISSION_REQUEST_READ_STORAGE:
               mReadStoragePermissionGranted = false;
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   mReadStoragePermissionGranted = true;
               }
               break;
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode == REQUEST_GET_PHOTO){
            if(resultCode == RESULT_OK){
                try {
                    mJourneyUri = data.getData();
                    mJourneyImageView.setImageURI( mJourneyUri );
                }catch (Exception e){
                    Snackbar.make( mConstraintLayout, R.string.photo_Selection_error , Snackbar.LENGTH_LONG ).show();
                }
            }
        }
    }

    private void lanchGalleryIntent(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory( Intent.CATEGORY_OPENABLE );
        intent.setType( "image/*" );
        startActivityForResult( Intent.createChooser( intent, getString( R.string.choose_photo ) ), REQUEST_GET_PHOTO );

    }
}
