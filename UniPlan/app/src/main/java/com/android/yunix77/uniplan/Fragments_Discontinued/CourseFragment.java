package com.android.yunix77.uniplan.Fragments_Discontinued;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.util.Log;

import com.android.yunix77.uniplan.AddCourseFragment;
import com.android.yunix77.uniplan.DatabaseControl;
import com.android.yunix77.uniplan.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CourseFragment extends Fragment {
    View               view;
    Button             addCourse;
    DatabaseControl db;
    LinearLayout       cardLinear;
    Spinner            term;
    ArrayList<Integer> term_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view       = inflater.inflate(R.layout.fragment_course, container, false);
        db         = new DatabaseControl(getActivity().getApplicationContext());
        cardLinear = (LinearLayout) view.findViewById(R.id.cardLinear);
        addCourse  = (Button) view.findViewById(R.id.addCourse);
        term       = (Spinner) view.findViewById(R.id.spinnerTerm);
        term_id    = new ArrayList<Integer>();

        fillTerm(term);
        fillCard();
        setAddCourseButton();

        return view;
    }

    public void setAddCourseButton() {
        if(term_id.size() == 0) {
            addCourse.setVisibility(View.GONE);
        }
        else {
            addCourse.setOnClickListener(new View.OnClickListener() {
                //Switch to AddTerm fragment - i.e. term input screen
                public void onClick(View v) {
                    final FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.include, new AddCourseFragment(term_id.get(term.getSelectedItemPosition())));
                    trans.addToBackStack(null);
                    trans.commit();
                }
            });
        }
    }
    public void fillTerm(Spinner term) {
        Cursor               terms    = db.getTerms();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        terms.moveToFirst();
        try {
            do {
                term_id.add(terms.getInt(terms.getColumnIndex("_id")));
                adapter.add("TERM: " + terms.getString(terms.getColumnIndex("START_DATE"))
                        + " to "
                        + terms.getString(terms.getColumnIndex("END_DATE")));
            } while (terms.moveToNext());
        } catch(Exception e) {
            adapter.add("No Term Found - Add Term First");
        }

        term.setAdapter(adapter);

        term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                Toast.makeText(getContext(), item, Toast.LENGTH_LONG).show();
                fillCard();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void fillCard() {
        Cursor cursor = null;
        int counter   = 0;
        if(term_id.size() != 0)
            cursor = db.getCourses(term_id.get(term.getSelectedItemPosition()));

        cardLinear.removeAllViews();

        if(cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    LayoutInflater inflater   = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.card_view_course, null);
                    CardView cardView         = (CardView) linearLayout.findViewById(R.id.cardView);
                    TextView textBox          = (TextView) linearLayout.findViewById(R.id.textBox);
                    int _id                   = cursor.getInt(cursor.getColumnIndex("_id"));

                    cardView.setTag(_id);

                    textBox.setText(cursor.getString(cursor.getColumnIndex("COURSE_CODE"))
                            + " - "
                            + cursor.getString(cursor.getColumnIndex("CNAME")));

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getContext(), String.valueOf(v.getTag()) + " course, show detail", Toast.LENGTH_SHORT).show();
                        }
                    });

                    cardLinear.addView(linearLayout, counter);
                    counter++;
                } while (cursor.moveToNext());
            }
            else {
                LayoutInflater inflater   = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.card_view_course, null);
                CardView cardView         = (CardView) linearLayout.findViewById(R.id.cardView);
                TextView textBox          = (TextView) linearLayout.findViewById(R.id.textBox);

                textBox.setText("No Course Found");
                cardLinear.addView(linearLayout);
            }
        }
    }
}
