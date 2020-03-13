package com.barmeg.travelerjourney;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class JourneyListFragment extends Fragment implements JourneysListAdapter.OnJourneyClickListener{

    private RecyclerView mRecyclerViewJourneys;
    private JourneysListAdapter mJourneysListAdapter;
    private ArrayList<Journey> mJourneys;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_journey_list, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        mRecyclerViewJourneys = view.findViewById( R.id.recycler_view_journey );
        mRecyclerViewJourneys.setLayoutManager( new LinearLayoutManager(getContext()) );

        mJourneys = new ArrayList<>();
        mJourneysListAdapter = new JourneysListAdapter( mJourneys, JourneyListFragment.this );
        mRecyclerViewJourneys.setAdapter( mJourneysListAdapter );

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection( "journeys").orderBy( "date" ).addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mJourneys.clear();
                if(e == null){
                    for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                        mJourneys.add(document.toObject( Journey.class ));

                    }
                    mJourneysListAdapter.notifyDataSetChanged();
                }
            }
        } );
                }

    @Override
    public void OnJourneyClick(Journey journey) {
        Intent intent = new Intent(getContext(), JourneyDetailsActivity.class);
        intent.putExtra( JourneyDetailsActivity.JOURNEY_DATA, journey );
        startActivity( intent );

    }
}
