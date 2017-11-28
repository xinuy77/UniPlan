package com.android.yunix77.uniplan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    int               t_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view       = inflater.inflate(R.layout.fragment_add_course, container, false);
        db         = new DatabaseControl(getActivity());
        submit     = (Button) view.findViewById(R.id.submitCourse);
        t_id       = this.getArguments().getInt("T_ID");

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                setUserInput();
                insertDB();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });



        return view;
    }

    private void insertDB() {
        String result = db.addCourse(t_id, input.get(1), input.get(0));
    }

    private void setUserInput() {
        EditText cname      = (EditText) view.findViewById(R.id.editCName);
        EditText ccode      = (EditText) view.findViewById(R.id.editCCode);
        input               = new ArrayList<String>();

        input.add(cname.getText().toString());
        input.add(ccode.getText().toString());
    }
}
