package com.example.mobilehome.actualstudent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mobilehome.studentfragment.ChatFragment;
import com.example.mobilehome.studentfragment.FriendFragment;
import com.example.mobilehome.studentfragment.RequestFragment;

public class StudentFragmentAdapter extends FragmentPagerAdapter {
    public StudentFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                StudentRequestFragment requestFragment = new StudentRequestFragment();
                return requestFragment;
            case 1:
                StudentChatFragment chatFragment = new StudentChatFragment();
                return chatFragment;
            case 2:
                StudentFrinedFragment friendFragment = new StudentFrinedFragment();
                return friendFragment;


        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "REQUESTS";
            case 1:
                return "CHATS";
            case 2:
                return "FRIENDS";
            default:
                return null;
        }
    }
}
