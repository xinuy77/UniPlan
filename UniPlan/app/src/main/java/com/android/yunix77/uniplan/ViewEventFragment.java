package com.android.yunix77.uniplan;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/*
    Fragment for list of Terms
    Includes
        Button to add Term object to database
            on select, opens AddTerm Fragment for data input
        ListView containing start/end dates of each term
            On select List item, opens Courses Fragment listing all courses for selected term
    Note
        Fragment is loaded on startup of app, and on selection of "Outline" from Navigation Drawer
 */
public class ViewEventFragment extends Fragment {
    //Fragment layout (UI)
    View myView;
    //Fragment UI components
    ListView eventDetails;
    //Database
    DatabaseControl db;
    //ID
    int event_id;
    //Course Code
    String c_code;
    @Nullable
    @Override
    //Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate Fragment layout
        myView = inflater.inflate(R.layout.fragment_viewevent, container, false);
        //Initialize UI components
        eventDetails = (ListView) myView.findViewById(R.id.eventdetails);
        //Initialize database
        db = new DatabaseControl(getActivity().getApplicationContext());
        //Get term ID from arguments
        event_id = this.getArguments().getInt("EVENT_ID");

        //Populate ListView
        SQLiteDatabase readDB = db.helper.getReadableDatabase();
        String query = "SELECT * FROM EVENT WHERE _id = ?";
        String[] args = {Integer.toString(event_id)};
        final Cursor cursor = readDB.rawQuery(query,args);
        ArrayList<String> eventStrArr = new ArrayList<String>();
        ArrayAdapter myAdapter;

        cursor.moveToFirst();
        int nameCol = cursor.getColumnIndex("ENAME");
        int typeCol = cursor.getColumnIndex("TYPE");
        int weightCol = cursor.getColumnIndex("WEIGHT");
        int gradeCol = cursor.getColumnIndex("GRADE");
        int dateCol = cursor.getColumnIndex("DATE");
        int stimeCol = cursor.getColumnIndex("START_TIME");
        int etimeCol = cursor.getColumnIndex("END_TIME");
        int locCol = cursor.getColumnIndex("LOCATION");
        int noteCol = cursor.getColumnIndex("NOTE");

        eventStrArr.add("NAME: " + cursor.getString(nameCol));

        String type;
        Toast.makeText(getActivity(), Integer.toString(cursor.getInt(typeCol)), Toast.LENGTH_SHORT).show();
        switch (cursor.getInt(typeCol)){
            case 0: type = "Test"; break;
            case 1: type = "Exam"; break;
            case 2: type = "Quiz"; break;
            case 3: type = "Assignment"; break;
            case 4: type = "Misc."; break;
            default: type = "Unknown"; break;
        }

        eventStrArr.add("TYPE: " + type);
        eventStrArr.add("WEIGHT: " + cursor.getInt(weightCol) + "% of Total Grade");
        if (cursor.getInt(gradeCol) < 0){
            eventStrArr.add("GRADE: N/A");
        }
        else {eventStrArr.add("GRADE: " + cursor.getInt(gradeCol) + "%");}
        eventStrArr.add("DATE: " + cursor.getString(dateCol));
        eventStrArr.add("STARTS AT: " + cursor.getString(stimeCol));
        eventStrArr.add("ENDS AT: " + cursor.getString(etimeCol));
        eventStrArr.add("LOCATION: " + cursor.getString(locCol));
        eventStrArr.add("NOTE: " + cursor.getString(noteCol));

        myAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, eventStrArr);
        eventDetails.setAdapter(myAdapter);

        return myView;
    }

}
