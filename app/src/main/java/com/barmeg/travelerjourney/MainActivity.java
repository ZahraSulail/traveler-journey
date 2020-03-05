package com.barmeg.travelerjourney;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Toolbar toolbar = findViewById( R.id.toolBar_home);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById( R.id.view_pager_home );
        TabLayout tabLayout = findViewById( R.id.tab_layout_home );

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment( new JourneyListFragment());
        pagerAdapter.addFragment( new JourneyListFragment());
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter( pagerAdapter );
    }





    class ViewPagerAdapter extends FragmentPagerAdapter {

       private final List<Fragment> fragments = new ArrayList<>();

       public ViewPagerAdapter(@NonNull FragmentManager fm ) {
           super( fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT );
       }

       @NonNull
       @Override
       public Fragment getItem(int position) {
           return fragments.get(position);
       }

       @Override
       public int getCount() {
           return fragments.size();
       }

       @Nullable
       @Override
       public CharSequence getPageTitle(int position) {
           switch (position){
               case 0:
                   return getString(R.string.journey_list);
               case 1:
                   return getString(R.string.journey_map);
                   default:
                       return null;
           }
       }
       public void addFragment(Fragment fragment){
           fragments.add( fragment );


       }
   }
}
