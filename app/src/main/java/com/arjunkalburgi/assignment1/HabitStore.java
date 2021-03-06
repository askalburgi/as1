package com.arjunkalburgi.assignment1;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun on 2016-09-26.
 */
public class HabitStore {
    List<iView> views = new ArrayList<iView>();
    List<Task> habits = new ArrayList<Task>();
    List<Task> historyHabits = new ArrayList<Task>();
    private static final String FILENAME = "file.sav";
    private static final String HistoryFILENAME = "hist_file.sav";
    private static final String TAG = "MainActivity";

    private static HabitStore instance = null;
    private HabitStore() {}

    public static synchronized HabitStore getInstance() {
        if (instance == null) instance = new HabitStore();
        return instance;
    }

    void addView(iView v) {
        views.add(v);
    }

    void removeView(iView v) {
        views.remove(v);
    }

    void notifyViewsOfChange() {
        for (iView v : views) {
            v.notifyChange();
        }
    }

    public void clear(Context context) {
        habits.clear();
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(habits, writer);
            writer.flush();

            //notifyViewsOfChange();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        historyHabits.clear();
        try {
            FileOutputStream fos = context.openFileOutput(HistoryFILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(historyHabits, writer);
            writer.flush();

            //notifyViewsOfChange();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // history habits stuff
    private void addHistoryHabit(Task t, Context context) {
        if (historyHabits.contains(t)) {
            t = historyHabits.get(historyHabits.indexOf(t));
            t.incrementNumTimesCompleted();
        } else {
            t.incrementNumTimesCompleted();
            historyHabits.add(t);
        }
        try {
            FileOutputStream fos = context.openFileOutput(HistoryFILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(historyHabits, writer);
            writer.flush();

            notifyViewsOfChange();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<Task> getHistoryHabits(Context context) {
        try {
            FileInputStream fis = context.openFileInput(HistoryFILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt TODAY
            Type lisType = new TypeToken<ArrayList<Task>>() {}.getType();
            historyHabits = gson.fromJson(in, lisType);

            return historyHabits;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return historyHabits;
    }

    public void deleteHistoryHabit(Task t, Context context) {
        if (historyHabits.contains(t)) {
            historyHabits.remove(t);
        }
        try {
            FileOutputStream fos = context.openFileOutput(HistoryFILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(historyHabits, writer);
            writer.flush();

            notifyViewsOfChange();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //habits stuff
    public void deleteHabit(Task t, Context context) {
        if (habits.contains(t)) {
            habits.remove(t);
        }
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(habits, writer);
            writer.flush();

            deleteHistoryHabit(t, context);
            notifyViewsOfChange();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void completeHabit(Task t, Context context) {
        habits.remove(t);
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(habits, writer);
            writer.flush();

            addHistoryHabit(t, context);
            notifyViewsOfChange();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveHabit(Task t, Context context) {
        if (habits.contains(t)) {
            return;
        } else if (historyHabits.contains(t)) {
            habits.add(historyHabits.get(historyHabits.indexOf(t)));
        } else {
            habits.add(t);
        }
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(habits, writer);
            writer.flush();

            notifyViewsOfChange();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<Task> getHabits(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt TODAY
            Type lisType = new TypeToken<ArrayList<Task>>() {}.getType();
            habits = gson.fromJson(in, lisType);

            return habits;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return habits;
    }
}
