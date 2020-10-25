package com.example.fypcommunicationtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ContactsFragment extends Fragment {

    private View myFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static com.example.fypcommunicationtool.ContactsFragment getInstance(){
        return new com.example.fypcommunicationtool.ContactsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_contacts, container, false);

        viewPager = myFragment.findViewById(R.id.contact_pager);
        tabLayout = myFragment.findViewById(R.id.contact_tabs);

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
        adapter.addFragment(new ContactListFragment(), "List of Contacts");
        adapter.addFragment(new RequestListFragment(), "List of Request");

        viewPager.setAdapter(adapter);
    }
}
