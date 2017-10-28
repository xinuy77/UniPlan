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
    Fragment for list of Instructors for a given course (ID passed as arg)
    Includes
        Button to add Instructor object to database
            on select, opens AddInstructor Fragment for data input
        ListView containing names of each instructor
            On select List item, opens ViewInstructor fragment providing details for selected instructor
 */
public class InstructorListFragment extends Fragment {
    //Fragment layout (UI)
    View myView;
    //Fragment UI components
    TextView listTitle;
    Button addInstructor;
    ListView instructorList;
    //Database
    DatabaseControl db;
    //Parent course ID
    int c_id;
    @Nullable
    @Override
    //Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate Fragment layout
        myView = inflater.inflate(R.layout.fragment_instructorlist, container, false);
        //Initialize UI components
        listTitle = (TextView) myView.findViewById(R.id.instructorlisttitle);
        addInstructor = (Button) myView.findViewById(R.id.addinstructor);
        instructorList = (ListView) myView.findViewById(R.id.instructorlist);

        //Initialize database
        db = new DatabaseControl(getActivity().getApplicationContext());
        //Initialize course ID
        c_id = this.getArguments().getInt("COURSE_ID");
        final String c_code = this.getArguments().getString("COURSE_CODE");

        //Initialize title
        listTitle.setText("Instructors for " + c_code);

        //Onclick "Add Course" button
        addInstructor.setOnClickListener(new View.OnClickListener(){
            //Switch to AddEvent fragment - i.e. event input screen
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

        //Populate List
        final Cursor cursor = db.getInstructors(c_id);
        ArrayList<String> instructorStrArr = new ArrayList<String>();
        ArrayAdapter myAdapter;

        int inName = cursor.getColumnIndex("INAME");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            instructorStrArr.add(cursor.getString(inName));
            cursor.moveToNext();
        }

        myAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, instructorStrArr);
        instructorList.setAdapter(myAdapter);

        //Onclick, pull up details of instructor for selected instructor
        instructorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View clickView,
                                    int position, long id) {
                //Get Term ID
                cursor.moveToFirst();
                cursor.moveToPosition(position);
                int term_id = cursor.getInt(0);

                //Bundle ID as argument to new fragment
                Bundle bundle = new Bundle(); //Create argument bundle
                bundle.putInt("INSTRUCTOR_ID", term_id); //Add term id to bundle
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
