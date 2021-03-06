package com.arjunkalburgi.assignment1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun on 2016-09-23.
 */
public class HabitsFragment extends Fragment implements iView {
    // Store instance variables
    private static final String TAG = "MainActivity";
    public static ListView oldTasksList;
    public static List<Task> taskList = new ArrayList<Task>();
    private ArrayAdapter<Task> taskAdapter;
    final HabitStore habitStore = HabitStore.getInstance();

    // newInstance constructor for creating fragment with arguments
    public static HabitsFragment newInstance() {
        HabitsFragment fragmentHabit = new HabitsFragment();
        return fragmentHabit;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habit_fragment_layout, container, false);

        this.taskAdapter = new TasksAdapter(this.getContext(), taskList);
        ListView list = (ListView) view.findViewById(R.id.list_todo);
        list.setAdapter(taskAdapter);

        return view;
    }

    public void notifyChange() {
        taskList = habitStore.getHabits(getActivity());
        taskAdapter.clear();
        taskAdapter.addAll(taskList);
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        habitStore.addView(this);
        taskList = habitStore.getHabits(getActivity());
        taskAdapter.clear();
        taskAdapter.addAll(taskList);
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        habitStore.removeView(this);
    }
}