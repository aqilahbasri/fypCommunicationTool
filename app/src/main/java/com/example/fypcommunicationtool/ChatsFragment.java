package com.example.fypcommunicationtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ChatsFragment extends Fragment {

    private View myFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public ChatsFragment() {
        // Required empty public constructor
    }

    public static com.example.fypcommunicationtool.ChatsFragment getInstance(){
        return new com.example.fypcommunicationtool.ChatsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_chats, container, false);

        viewPager = myFragment.findViewById(R.id.chat_pager);
        tabLayout = myFragment.findViewById(R.id.chat_tabs);

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
        adapter.addFragment(new com.example.fypcommunicationtool.ChatsPrivateFragment(), "Private Chats");
        adapter.addFragment(new com.example.fypcommunicationtool.ChatsGroupFragment(), "Group Chats");

        viewPager.setAdapter(adapter);
    }
}
