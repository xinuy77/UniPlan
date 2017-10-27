package com.android.yunix77.uniplan;


import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
public class OutlineFragment extends Fragment {
    //Fragment layout (UI)
    View myView;
    //Fragment UI components

    Button addTerm;
    ListView termList;
    //Database
    DatabaseControl db;
    @Nullable
    @Override
    //Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate Fragment layout
        myView = inflater.inflate(R.layout.fragment_outline, container, false);
        //Initialize database
        db = new DatabaseControl(getActivity().getApplicationContext());

        //Onclick "Add Term" button
        addTerm = (Button) myView.findViewById(R.id.addterm); //Initialize button
        addTerm.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment - i.e. term input screen
            public void onClick(View v){

                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, new AddTermFragment());
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        //Populate List
        final Cursor cursor = db.getTerms();
        final ArrayList<String> termStrArr = new ArrayList<String>();
        ArrayAdapter myAdapter;

        int startCol = cursor.getColumnIndex("START_DATE");
        int endCol = cursor.getColumnIndex("END_DATE");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            termStrArr.add(cursor.getString(startCol)
                    + " to "
                    + cursor.getString(endCol));
            cursor.moveToNext();
        }

        myAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, termStrArr);
        termList = (ListView) myView.findViewById(R.id.termlist);
        termList.setAdapter(myAdapter);

        //Onclick, pull up ViewTerm with selected term as parent.
        termList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View clickView,
                                    int position, long id) {
                //Get Term ID
                cursor.moveToFirst();
                cursor.moveToPosition(position);
                int term_id = cursor.getInt(0);

                //Bundle ID as argument to new fragment
                Bundle bundle = new Bundle(); //Create argument bundle
                bundle.putInt("TERM_ID", term_id); //Add term id to bundle
                bundle.putString("TERM_TITLE", "Term: " + termStrArr.get(position));
                ViewTermFragment term = new ViewTermFragment(); //Create new Courses fragment (to be implemented)
                term.setArguments(bundle); //Attach arguments to fragment

                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, term);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        return myView;
    }

}
