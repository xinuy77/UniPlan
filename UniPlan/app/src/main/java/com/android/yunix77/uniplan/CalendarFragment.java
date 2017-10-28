package com.android.yunix77.uniplan;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yunix77.uniplan.Fragments_Discontinued.CalendarCustomView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ra on 2017-10-25.
 */

public class CalendarFragment extends Fragment {

    View myView;
    DatabaseControl db;
    private static final String TAG = CalendarCustomView.class.getSimpleName();
    private Button previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Button addEventButton;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private GridAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_calendar, container, false);
        db = new DatabaseControl(getActivity());

        previousButton = (Button)myView.findViewById(R.id.previous_month);
        nextButton = (Button)myView.findViewById(R.id.next_month);
        currentDate = (TextView)myView.findViewById(R.id.display_current_date);
        calendarGridView = (GridView)myView.findViewById(R.id.calendar_grid);

        setUpCalendarAdapter();

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
            }
        });

        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(getActivity(),"Clicked" + (position + 1), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return myView;
    }

    private void setUpCalendarAdapter(){
        List<Date> dayValueInCells = new ArrayList<Date>();
        List<Date> mEvents = new ArrayList<Date>();

        String[] args = {};
        Cursor c = db.helper.getReadableDatabase().rawQuery("SELECT DATE FROM EVENT", args);
        int dateCol = c.getColumnIndex("DATE");
        DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");

        c.moveToFirst();
        while (!c.isAfterLast()){
            try {
                String dateString = c.getString(dateCol);
                mEvents.add(dateFormat.parse(dateString));
            }
            catch(java.text.ParseException e) {
                e.printStackTrace();
            }
            c.moveToNext();
        }

        Calendar mCal = (Calendar)cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "Number of date " + dayValueInCells.size());
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mAdapter = new GridAdapter(getActivity(), dayValueInCells, cal, mEvents);
        calendarGridView.setAdapter(mAdapter);
    }
}
