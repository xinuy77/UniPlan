package com.android.yunix77.uniplan;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/*
    Fragment for list of Events for a given course (ID passed as arg)
    Includes
        Button to add Event object to database
            on select, opens AddEvent Fragment for data input
        ListView containing names of each event
            On select List item, opens ViewEvent fragment providing details for selected event
 */
public class CalEventFragment extends Fragment {
    //Fragment layout (UI)
    View myView;
    //Fragment UI components
    TextView listTitle;
    ListView eventList;
    //Database
    DatabaseControl db;
    //Parent course ID
    String date;
    @Nullable
    @Override
    //Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate Fragment layout
        myView = inflater.inflate(R.layout.fragment_eventlist, container, false);
        //Initialize UI components
        listTitle = (TextView) myView.findViewById(R.id.eventlisttitle);
        eventList = (ListView) myView.findViewById(R.id.eventlist);

        //Initialize database
        db = new DatabaseControl(getActivity().getApplicationContext());
        //Initialize course ID
        date = this.getArguments().getString("DATE");
        final String c_code = this.getArguments().getString("COURSE_CODE");

        View button = myView.findViewById(R.id.addevent);
        button.setVisibility(View.GONE);

        //Initialize title
        listTitle.setText("Events for " + date);

        //Populate List
        String[] args = {date};
        final Cursor cursor = db.helper.getReadableDatabase().rawQuery("SELECT * FROM EVENT WHERE DATE = ?", args);
        ArrayList<String> eventStrArr = new ArrayList<String>();
        ArrayAdapter myAdapter;

        int evName = cursor.getColumnIndex("ENAME");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            eventStrArr.add(cursor.getString(evName));
            cursor.moveToNext();
        }

        myAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, eventStrArr);
        eventList.setAdapter(myAdapter);

        //Onclick, pull up details of event for selected event
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View clickView,
                                    int position, long id) {
                //Get Term ID
                cursor.moveToFirst();
                cursor.moveToPosition(position);
                int term_id = cursor.getInt(0);

                //Bundle ID as argument to new fragment
                Bundle bundle = new Bundle(); //Create argument bundle
                bundle.putInt("EVENT_ID", term_id); //Add term id to bundle
                ViewEventFragment event = new ViewEventFragment(); //Create new Courses fragment (to be implemented)
                event.setArguments(bundle); //Attach arguments to fragment

                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, event);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        return myView;
    }

}
