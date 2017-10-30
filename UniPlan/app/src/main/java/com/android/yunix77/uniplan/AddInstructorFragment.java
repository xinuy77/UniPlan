package com.android.yunix77.uniplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * Created by Ra on 2017-10-24.
 */

public class AddInstructorFragment extends Fragment {
    //Database
    DatabaseControl db;

    //UI Components
    View myView;
    EditText addIName;
    Spinner addInstructorStatus;
    EditText addInstructorEmail;
    Button submit;

    //Variables to be stored in DB
    int courseID;
    String iName;
    int status;
    String email;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_addinstructor, container, false);
        db = new DatabaseControl(getActivity());

        //Initialize components
        addIName = (EditText) myView.findViewById(R.id.addiname);
        addInstructorStatus = (Spinner) myView.findViewById(R.id.addinstructorstatus);
        addInstructorEmail = (EditText) myView.findViewById(R.id.addinstructoremail);
        submit = (Button) myView.findViewById(R.id.submitInstructor);

        courseID = this.getArguments().getInt("COURSE_ID");

        //Onclick "Add Term" button
        submit.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment
            public void onClick(View v){
                iName = addIName.getText().toString();
                status = addInstructorStatus.getSelectedItemPosition();
                email = addInstructorEmail.getText().toString();

                if (iName.isEmpty()){
                    Toast toast = Toast.makeText(getActivity(),"Please enter NAME", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    //Add event to database
                    db.addInstructor(iName,courseID,status,email);
                    //Return to previous page
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                }
            }
        });
        return myView;
    }
}
