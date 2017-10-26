package com.android.yunix77.uniplan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import android.util.Log;

public class AddCourseFragment extends Fragment {
    DatabaseControl   db;
    View              view;
    Button            submit;
    ArrayList<String> input;
    Spinner           day;
    int               t_id;

    public AddCourseFragment(int t_id) {
        this.t_id = t_id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view   = inflater.inflate(R.layout.fragment_add_course, container, false);
        db     = new DatabaseControl(getActivity());
        submit = (Button) view.findViewById(R.id.submitCourse);
        day    = (Spinner) view.findViewById(R.id.spinnerDay);

        fillDays(day);

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setUserInput();
                insertDB();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }

    public void insertDB() {
        Cursor cursor;
        int    c_id;
        String result = db.addCourse(t_id, input.get(1), input.get(0));
        cursor        = db.getLatestCourse();

        cursor.moveToFirst();
        try {
            do {
                c_id   = cursor.getInt(cursor.getColumnIndex("_id"));
                result = db.addInstructor(input.get(4), c_id, 0,"nullForNow");
                result = db.addTime(c_id, 0, input.get(2), null, "nullForNow", "nullForNow", day.getSelectedItemPosition());
            } while (cursor.moveToNext());
        } catch(Exception e) {
            Log.i("DEBUG", "Err: "+ e.toString());
        }

    }

    public void fillDays(Spinner day) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add("Monday");
        adapter.add("Tuesday");
        adapter.add("Wednesday");
        adapter.add("Thursday");
        adapter.add("Friday");
        adapter.add("Saturday");
        adapter.add("Sunday");
        day.setAdapter(adapter);
    }

    public void setUserInput() {
        EditText cname      = (EditText) view.findViewById(R.id.editCName);
        EditText ccode      = (EditText) view.findViewById(R.id.editCCode);
        EditText location   = (EditText) view.findViewById(R.id.editLocation);
        EditText instructor = (EditText) view.findViewById(R.id.editInstructor);
        input               = new ArrayList<String>();

        input.add(cname.getText().toString());
        input.add(ccode.getText().toString());
        input.add(location.getText().toString());
        input.add(instructor.getText().toString());
        input.add(day.getSelectedItem().toString());
    }
}
