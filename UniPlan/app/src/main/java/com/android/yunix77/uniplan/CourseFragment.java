package com.android.yunix77.uniplan;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CourseFragment extends Fragment {
    View               view;
    Button             addCourse, addTerm, viewTerm;
    DatabaseControl    db;
    LinearLayout       cardLinear;
    Spinner            term;
    ArrayList<Integer> term_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view       = inflater.inflate(R.layout.fragment_course, container, false);
        db         = new DatabaseControl(getActivity().getApplicationContext());
        cardLinear = (LinearLayout) view.findViewById(R.id.cardLinear);
        addCourse  = (Button) view.findViewById(R.id.addCourse);
        addTerm    = (Button) view.findViewById(R.id.addTerm);
        viewTerm   = (Button) view.findViewById(R.id.termDetail);
        term       = (Spinner) view.findViewById(R.id.spinnerTerm);
        term_id    = new ArrayList<Integer>();

        fillTerm(term);
        fillCard();
        setAddCourseButton();
        setAddTermButton();
        setViewTermButton();

        return view;
    }

    private void setViewTermButton() {
        if(term_id.size() == 0) {
            viewTerm.setVisibility(View.GONE);
        }
        else {
            viewTerm.setOnClickListener(new View.OnClickListener() {
                //Switch to AddTerm fragment - i.e. term input screen
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("TERM_ID", term_id.get(term.getSelectedItemPosition()));

                    ViewTermFragment term = new ViewTermFragment();
                    term.setArguments(bundle);

                    final FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.include, term);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            });
        }
    }

    private void setAddTermButton() {
        addTerm.setOnClickListener(new View.OnClickListener() {
            //Switch to AddTerm fragment - i.e. term input screen
            public void onClick(View v) {
                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, new AddTermFragment());
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }

    private void setAddCourseButton() {
        if(term_id.size() == 0) {
            addCourse.setVisibility(View.GONE);
        }
        else {
            addCourse.setOnClickListener(new View.OnClickListener() {
                //Switch to AddTerm fragment - i.e. term input screen
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("T_ID", term_id.get(term.getSelectedItemPosition()));

                    AddCourseFragment add_course = new AddCourseFragment();
                    add_course.setArguments(bundle);

                    final FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.include, add_course);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            });
        }
    }

    private void fillTerm(Spinner term) {
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

    private void fillCard() {
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

                            Bundle bundle = new Bundle();
                            bundle.putInt("C_ID", (Integer)v.getTag());

                            ViewCourseFragment c_detail = new ViewCourseFragment();
                            c_detail.setArguments(bundle);

                            final FragmentTransaction trans = getFragmentManager().beginTransaction();
                            trans.replace(R.id.include, c_detail);
                            trans.addToBackStack(null);
                            trans.commit();
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
