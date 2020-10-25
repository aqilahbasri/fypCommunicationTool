package com.example.fypcommunicationtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class GIFLibraryFragment extends Fragment {

    private View myFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public GIFLibraryFragment() {
        // Required empty public constructor
    }

    public static com.example.fypcommunicationtool.GIFLibraryFragment getInstance(){
        return new com.example.fypcommunicationtool.GIFLibraryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_gif_library, container, false);

        viewPager = myFragment.findViewById(R.id.gif_library_pager);
        tabLayout = myFragment.findViewById(R.id.gif_library_tabs);

        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        com.example.fypcommunicationtool.TabsPagerAdapter adapter = new com.example.fypcommunicationtool.TabsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new com.example.fypcommunicationtool.MyGIFFragment(), "My GIF");
        adapter.addFragment(new com.example.fypcommunicationtool.FavouriteGIFFragment(), "Favourite GIF");
        adapter.addFragment(new com.example.fypcommunicationtool.PendingGIFFragment(), "Pending GIF");

        viewPager.setAdapter(adapter);
    }
}
