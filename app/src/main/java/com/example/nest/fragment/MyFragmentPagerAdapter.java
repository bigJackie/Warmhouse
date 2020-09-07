package com.example.nest.fragment;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nest.CommunityPage;
import com.example.nest.HealthPage;
import com.example.nest.HomeContainer;
import com.example.nest.MainFragment;
import com.example.nest.PersonPage;

public class MyFragmentPagerAdapter extends FragmentStateAdapter {

    public  HomeContainer homeContainer = null;
    private CommunityPage communityPage = null;
    private HealthPage healthPage = null;
    private PersonPage personPage = null;
    private SparseArray<Fragment> fragments = new SparseArray();

    public MyFragmentPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        homeContainer = new HomeContainer();
        communityPage= new CommunityPage();
        healthPage = new HealthPage();
        personPage = new PersonPage();
        fragments.put(MainFragment.PAGE_HOME, homeContainer);
        fragments.put(MainFragment.PAGE_COMMUNITY, communityPage);
        fragments.put(MainFragment.PAGE_HEALTH, healthPage);
        fragments.put(MainFragment.PAGE_PERSON, personPage);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}