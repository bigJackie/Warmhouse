package com.example.nest.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.haibin.calendarview.CalendarView;

import androidx.fragment.app.Fragment;

import com.example.nest.R;

public class CalenderPage extends Fragment {

    private CalendarView calendar;


    public CalenderPage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_page, container, false);
        bindView(view);
        initView();
        return view;
    }

    private void bindView(View view) {
        calendar = view.findViewById(R.id.calendarView);
    }

    private void initView() {

    }
}