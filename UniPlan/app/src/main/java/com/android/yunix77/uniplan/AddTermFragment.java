package com.android.yunix77.uniplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.Date;


/**
 * Created by Ra on 2017-10-24.
 */

public class AddTermFragment extends Fragment {
    //Database
    DatabaseControl db;
    //UI Components
    View myView;
    DatePicker sdatePicker;
    DatePicker edatePicker;
    Button submit;
    //Start date components
    int sd;
    int sm;
    int sy;
    //end date components
    int ed;
    int em;
    int ey;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_addterm, container, false);
        db = new DatabaseControl(getActivity());
        //Initialize components
        sdatePicker = (DatePicker) myView.findViewById(R.id.setTermStart);
        edatePicker = (DatePicker) myView.findViewById(R.id.setTermEnd);
        submit = (Button) myView.findViewById(R.id.submitTerm);

        //Onclick "Add Term" button
        submit.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment
            public void onClick(View v){
                sd = sdatePicker.getDayOfMonth();
                sm = sdatePicker.getMonth();
                sy = sdatePicker.getYear();
                ed = edatePicker.getDayOfMonth();
                em = edatePicker.getMonth();
                ey = edatePicker.getYear();
                Date sdate = new Date(sy,sm,sd);
                Date edate = new Date(ey,em,ed);

                //If dates are valid (i.e. star date < end date)
                //  add term to DB and return to Term List
                if (sdate.before(edate)){
                    //Formate dates as "YYYY-MM-DD" strings
                    String sdateString = Integer.toString(sy)
                            + "-"
                            + Integer.toString(sm)
                            + "-"
                            + Integer.toString(sd);
                    String edateString = Integer.toString(ey)
                            + "-"
                            + Integer.toString(em)
                            + "-"
                            + Integer.toString(ed);
                    //Determine semester
                    int sem;
                    if ((9 <= sm) && (sm <= 12)){
                        sem = 1;
                    }
                    else if ((1 <= sm) && (sm <= 4)){
                        sem = 2;
                    }
                    else {sem = 3;}
                    //Add Term to database
                    db.addTerm(sy, sem, sdateString, edateString);
                    //Return to previous page
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                }
                else{
                    Toast toast = Toast.makeText(getActivity(),"Start Date must come before End Date", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        return myView;
    }
}
