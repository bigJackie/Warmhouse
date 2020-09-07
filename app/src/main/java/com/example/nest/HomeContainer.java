package com.example.nest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nest.home.CalenderPage;
import com.example.nest.home.ChatsPage;
import com.example.nest.home.ContactsPage;
import com.example.nest.home.GiftPage;
import com.example.nest.home.HomePage;

public class HomeContainer extends Fragment {
    private static Fragment chatsPage;
    private static Fragment contactsPage;
    private static Fragment homePage;
    private static Fragment calendarPage;
    private static Fragment giftPage;
    private static FragmentTransaction transaction;

    public HomeContainer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_container, container, false);
        chatsPage =  new ChatsPage();
        contactsPage = new ContactsPage();
        homePage = new HomePage();
        calendarPage = new CalenderPage();
        giftPage = new GiftPage();
        transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.home_container, homePage).commit();
        bindFragment();
        return view;
    }

    private void bindFragment() {
        MainFragment.fragmentArray[0] = homePage;
        MainFragment.fragmentArray[1] = chatsPage;
        MainFragment.fragmentArray[2] = contactsPage;
        MainFragment.fragmentArray[3] = calendarPage;
        MainFragment.fragmentArray[4] = giftPage;
    }

    void changeChildFragment(Fragment fragment){
        transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                .replace(R.id.home_container, fragment)
                .commit();

    }

}

