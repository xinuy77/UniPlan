package com.android.yunix77.uniplan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewCourseFragment extends Fragment {

    View            view;
    DatabaseControl db;
    ListView        c_details, i_details;
    Button          addInstructors, viewEvents, removeCourse;
    int             c_id;
    String          c_code;

    ArrayList<Integer> i_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view            = inflater.inflate(R.layout.fragment_viewcourse, container, false);
        db              = new DatabaseControl(getActivity().getApplicationContext());
        c_details       = (ListView) view.findViewById(R.id.course_details);
        i_details       = (ListView) view.findViewById(R.id.instructor_details);
        c_id            =  this.getArguments().getInt("C_ID");
        addInstructors  = (Button) view.findViewById(R.id.addinstructors);
        viewEvents      = (Button) view.findViewById(R.id.viewevents);
        removeCourse    = (Button) view.findViewById(R.id.removeCourse);

        fillList();
        setAddInstructorsListener();
        setViewEventsListener();
        setRemoveCourseListener();

        return view;
    }

    private void removeCourse() {

    }

    private void setRemoveCourseListener() {
        removeCourse.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment - i.e. term input screen
            public void onClick(View v){
                db.deleteCourse(c_id);

                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void setViewEventsListener() {
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
    }

    private void setAddInstructorsListener() {
        addInstructors.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment - i.e. term input screen
            public void onClick(View v){

                Bundle bundle = new Bundle();
                bundle.putInt("COURSE_ID", c_id);
                AddInstructorFragment addInstructor = new AddInstructorFragment();
                addInstructor.setArguments(bundle);
                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, addInstructor);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }

    private void fillList() {
        Cursor[] cursor                = {db.getCourseById(c_id), db.getTime(c_id, 0), db.getInstructors(c_id)};
        String[] days                  = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayList<String> details      = new ArrayList<String>();
        ArrayList<String> details_inst = new ArrayList<String>();
                          i_id         = new ArrayList<Integer>();
        ArrayAdapter      adapter, inst_adapter;

        for(int i = 0; i < cursor.length; i++) {
                cursor[i].moveToFirst();
                do {
                    if(i == 0) {
                        c_code = cursor[i].getString(cursor[i].getColumnIndex("COURSE_CODE"));
                        details.add("Course Code: " + c_code);
                        details.add("Course Name: " + cursor[i].getString(cursor[i].getColumnIndex("CNAME")));
                    }
                    else if(i == 1) {
                        details.add("Location: " + cursor[i].getString(cursor[i].getColumnIndex("LOCATION")));
                        details.add("Time: "
                                + cursor[i].getString(cursor[i].getColumnIndex("START_TIME"))
                                + " - "
                                + cursor[i].getString(cursor[i].getColumnIndex("END_TIME")));
                        details.add("Day: " + days[Integer.parseInt(cursor[i].getString(cursor[i].getColumnIndex("DAY")))]);
                    }
                    else {
                        if(cursor[i].getCount() > 0) {
                            i_id.add(cursor[i].getInt(cursor[i].getColumnIndex("_id")));

                            String i_name = cursor[i].getString(cursor[i].getColumnIndex("INAME"));
                            int status    = cursor[i].getInt(cursor[i].getColumnIndex("STATUS"));

                            if (status == 0) {
                                details_inst.add("Professor: " + i_name);
                            } else {
                                details_inst.add("TA: " + i_name);
                            }
                        }
                    }
                } while (cursor[i].moveToNext());
        }

        adapter      = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, details);
        inst_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, details_inst);

        c_details.setAdapter(adapter);
        i_details.setAdapter(inst_adapter);
        setClickEventOnInstructors();
    }

    private void setClickEventOnInstructors() {
        i_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View clickView,
                                    int position, long id) {

                //Bundle ID as argument to new fragment
                Bundle bundle = new Bundle(); //Create argument bundle
                bundle.putInt("INSTRUCTOR_ID", i_id.get(position)); //Add term id to bundle

                ViewInstructorFragment event = new ViewInstructorFragment(); //Create new Courses fragment (to be implemented)
                event.setArguments(bundle); //Attach arguments to fragment

                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, event);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }
}
