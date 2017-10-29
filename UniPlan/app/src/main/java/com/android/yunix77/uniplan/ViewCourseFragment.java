package com.android.yunix77.uniplan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewCourseFragment extends Fragment {

    View            view;
    DatabaseControl db;
    ListView        c_details;
    int             c_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view         = inflater.inflate(R.layout.fragment_viewcourse, container, false);
        db           = new DatabaseControl(getActivity().getApplicationContext());
        c_details    = (ListView) view.findViewById(R.id.course_details);
        c_id         =  this.getArguments().getInt("C_ID");

        fillList();

        return view;
    }

    private void fillList() {
        Cursor[] cursor           = {db.getCourseById(c_id), db.getTime(c_id, 0), db.getInstructors(c_id)};
        String[] days             = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayList<String> details = new ArrayList<String>();
        ArrayAdapter      adapter;

        for(int i = 0; i < cursor.length; i++) {
                cursor[i].moveToFirst();
                do {
                    if(i == 0) {
                        details.add("Course Code: " + cursor[i].getString(cursor[i].getColumnIndex("COURSE_CODE")));
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
                        String i_name = cursor[i].getString(cursor[i].getColumnIndex("INAME"));
                        int    status = cursor[i].getInt(cursor[i].getColumnIndex("STATUS"));
                        Log.i("DEBUG", "Inst Name:" + i_name);
                        if(status == 0) {
                            details.add("Professor: " + i_name);
                        }
                        else {
                            details.add("TA: " + i_name);
                        }
                    }
                } while (cursor[i].moveToNext());
        }

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, details);
        c_details.setAdapter(adapter);
    }
}
