package com.android.yunix77.uniplan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewCourseFragment extends Fragment {

    View            view;
    DatabaseControl db;
    ListView        c_details;
    Button          viewInstructors, viewEvents;
    int             c_id;
    String          c_code;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view         = inflater.inflate(R.layout.fragment_viewcourse, container, false);
        db           = new DatabaseControl(getActivity().getApplicationContext());
        c_details    = (ListView) view.findViewById(R.id.course_details);
        c_id         =  this.getArguments().getInt("C_ID");

        fillList();

        viewInstructors = (Button) view.findViewById(R.id.viewinstructors);
        viewEvents = (Button) view.findViewById(R.id.viewevents);

        viewInstructors.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment - i.e. term input screen
            public void onClick(View v){

                Bundle bundle = new Bundle(); //Create argument bundle
                bundle.putInt("COURSE_ID", c_id); //Add course id to bundle
                bundle.putString("COURSE_CODE", c_code);
                InstructorListFragment instructors = new InstructorListFragment(); //Create new Instructorlist fragment (to be implemented)
                instructors.setArguments(bundle); //Attach arguments to fragment

                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, instructors);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        viewEvents.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment - i.e. term input screen
            public void onClick(View v){

                Bundle bundle = new Bundle(); //Create argument bundle
                bundle.putInt("COURSE_ID", c_id); //Add course id to bundle
                bundle.putString("COURSE_CODE", c_code);
                EventListFragment events = new EventListFragment(); //Create new Instructorlist fragment (to be implemented)
                events.setArguments(bundle); //Attach arguments to fragment

                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, events);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        return view;
    }

    private void fillList() {
        Cursor[] cursor           = {db.getCourseById(c_id), db.getTime(c_id, 0)};
        String[] days             = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayList<String> details = new ArrayList<String>();
        ArrayAdapter      adapter;

        for(int i = 0; i < cursor.length; i++) {
                cursor[i].moveToFirst();
                do {
                    if(i == 0) {
                        c_code = cursor[i].getString(cursor[i].getColumnIndex("COURSE_CODE"));
                        details.add("Course Code: " + c_code);
                        details.add("Course Name: " + cursor[i].getString(cursor[i].getColumnIndex("CNAME")));
                    }
                    else {
                        details.add("Location: " + cursor[i].getString(cursor[i].getColumnIndex("LOCATION")));
                        details.add("Time: "
                                + cursor[i].getString(cursor[i].getColumnIndex("START_TIME"))
                                + " - "
                                + cursor[i].getString(cursor[i].getColumnIndex("END_TIME")));
                        details.add("Day: " + days[Integer.parseInt(cursor[i].getString(cursor[i].getColumnIndex("DAY")))]);
                    }
                } while (cursor[i].moveToNext());
        }

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, details);
        c_details.setAdapter(adapter);
    }
}
