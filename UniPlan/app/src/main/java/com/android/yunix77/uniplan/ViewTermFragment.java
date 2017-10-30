package com.android.yunix77.uniplan;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.w3c.dom.Text;

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
public class ViewTermFragment extends Fragment {
    //Fragment layout (UI)
    View myView;
    //Fragment UI components
    Button viewCourses;
    ListView termDetails;
    //Database
    DatabaseControl db;
    //ID
    int term_id;
    @Nullable
    @Override
    //Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate Fragment layout
        myView = inflater.inflate(R.layout.fragment_viewterm, container, false);
        //Initialize UI components
        viewCourses = (Button) myView.findViewById(R.id.viewcourses);
        termDetails = (ListView) myView.findViewById(R.id.termdetails);
        //Initialize database
        db = new DatabaseControl(getActivity().getApplicationContext());
        //Get term ID from arguments
        term_id = this.getArguments().getInt("TERM_ID");

        //Onclick "View Courses" button
        //Commented out until CourseListFragment or equivalent is implemented
        viewCourses.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment - i.e. term input screen
            public void onClick(View v){

                Bundle bundle = new Bundle(); //Create argument bundle
                bundle.putInt("TERM_ID", term_id); //Add term id to bundle
                CourseFragment courses = new CourseFragment(); //Create new Courses fragment (to be implemented)
                courses.setArguments(bundle); //Attach arguments to fragment

                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, courses);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        //Populate ListView
        SQLiteDatabase readDB = db.helper.getReadableDatabase();
        String query = "SELECT * FROM TERM_SCHEDULE WHERE _id = ?";
        String[] args = {Integer.toString(term_id)};
        final Cursor cursor = readDB.rawQuery(query,args);
        ArrayList<String> termStrArr = new ArrayList<String>();
        ArrayAdapter myAdapter;

        cursor.moveToFirst();
        int yearCol = cursor.getColumnIndex("YEAR");
        int semCol = cursor.getColumnIndex("SEMESTER");
        int startCol = cursor.getColumnIndex("START_DATE");
        int endCol = cursor.getColumnIndex("END_DATE");

        String year = Integer.toString(cursor.getInt(yearCol));
        termStrArr.add("YEAR: " + year);

        String semester;
        switch (cursor.getInt(semCol)){
            case 1: semester = "Fall";
            case 2: semester = "Winter";
            case 3: semester = "Summer";
            default: semester = "Unknown";
        }
        termStrArr.add("SEMESTER: " + semester);

        termStrArr.add("START DATE: " + cursor.getString(startCol));
        termStrArr.add("END DATE: " + cursor.getString(endCol));

        myAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, termStrArr);
        termDetails.setAdapter(myAdapter);

        return myView;
    }

}
