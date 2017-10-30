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
import android.widget.TimePicker;


public class AddCourseFragment extends Fragment {
    DatabaseControl   db;
    View              view;
    Button            submit;
    ArrayList<String> input;
    Spinner           day;
    TimePicker        start_time, end_time;
    int               t_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view       = inflater.inflate(R.layout.fragment_add_course, container, false);
        db         = new DatabaseControl(getActivity());
        submit     = (Button) view.findViewById(R.id.submitCourse);
        day        = (Spinner) view.findViewById(R.id.spinnerDay);
        t_id       = this.getArguments().getInt("T_ID");
        start_time = (TimePicker) view.findViewById(R.id.startTime);
        end_time   = (TimePicker) view.findViewById(R.id.endTime);

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
    public String formatTime(TimePicker time) {
        String output = "";
        int    hour   = time.getCurrentHour();
        int    min    = time.getCurrentMinute();

        if(hour < 10)
            output += "0" + hour + ":";
        else
            output += hour + ":";
        if(min < 10)
            output += "0" + min;
        else
            output += min;

        return output;
    }

    private void insertDB() {
        Cursor cursor;
        int    c_id;
        String result = db.addCourse(t_id, input.get(1), input.get(0));
        String s_time = formatTime(start_time);
        String e_time = formatTime(end_time);
        cursor        = db.getLatestCourse();

        cursor.moveToFirst();
        try {
            do {
                c_id   = cursor.getInt(cursor.getColumnIndex("_id"));
                result = db.addTime(c_id, 0, input.get(2), null, s_time, e_time, day.getSelectedItemPosition());
            } while (cursor.moveToNext());
        } catch(Exception e) {
            Log.i("DEBUG", "Err: "+ e.toString());
        }

    }

    private void fillDays(Spinner day) {
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

    private void setUserInput() {
        EditText cname      = (EditText) view.findViewById(R.id.editCName);
        EditText ccode      = (EditText) view.findViewById(R.id.editCCode);
        EditText location   = (EditText) view.findViewById(R.id.editLocation);
        input               = new ArrayList<String>();

        input.add(cname.getText().toString());
        input.add(ccode.getText().toString());
        input.add(location.getText().toString());
        input.add(day.getSelectedItem().toString());
    }
}
