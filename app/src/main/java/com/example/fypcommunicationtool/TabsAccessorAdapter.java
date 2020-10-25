package com.example.fypcommunicationtool;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0  : ChatsFragment chatsFragment = new ChatsFragment();
                      return chatsFragment;
            case 1  : com.example.fypcommunicationtool.GIFLibraryFragment gifLibraryFragment = new com.example.fypcommunicationtool.GIFLibraryFragment();
                      return gifLibraryFragment;
            case 2  : com.example.fypcommunicationtool.ContactsFragment contactsFragment = new com.example.fypcommunicationtool.ContactsFragment();
                      return contactsFragment;
            default : return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0  : return "Chats";
            case 1  : return "GIF Library";
            case 2  : return "Contacts";
            default : return null;
        }
    }
}
