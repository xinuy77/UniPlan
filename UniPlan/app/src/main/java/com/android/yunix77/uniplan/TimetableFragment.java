package com.android.yunix77.uniplan;


import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
    Fragment for list of Events for a given course (ID passed as arg)
    Includes
        Button to add Event object to database
            on select, opens AddEvent Fragment for data input
        ListView containing names of each event
            On select List item, opens ViewEvent fragment providing details for selected event
 */
public class TimetableFragment extends Fragment {
    //Fragment layout (UI)
    View myView;
    //Fragment UI components
    ListView           list;
    Spinner            term;
    Spinner            day;
    //Listview items
    ArrayList<String> item = new ArrayList<>();
    ArrayList<String> subItem = new ArrayList<>();
    ArrayList<String> itemTime = new ArrayList<>();

    //Database
    DatabaseControl db;

    ArrayList<Integer> term_id;
    int currentDay;
    @Nullable
    @Override
    //Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate Fragment layout
        myView = inflater.inflate(R.layout.fragment_timetable, container, false);
        //Initialize UI components
        list = (ListView) myView.findViewById(R.id.list);
        term = (Spinner) myView.findViewById(R.id.spinnerTerm);
        day = (Spinner) myView.findViewById(R.id.spinnerDay);
        term_id    = new ArrayList<Integer>();


        //Initialize database
        db = new DatabaseControl(getActivity().getApplicationContext());

        fillTerm(term);
        fillDay(day);
        Log.d("NOTE", "All good to the end");
        return myView;
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
                fillList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void fillDay(Spinner day) {
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
        day.setAdapter(adapter);

        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("NOTE", "Day Selected");
                Spinner spinner = (Spinner) parent;
                String i = (String) spinner.getSelectedItem();
                Toast.makeText(getContext(), i, Toast.LENGTH_LONG).show();
                currentDay = position;
                Log.d("NOTE", "Current Day: " + Integer.toString(currentDay));
                fillList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    private void fillList(){
        item.clear();
        subItem.clear();
        itemTime.clear();

        //Get array of all courses for current term
        ArrayList<Integer> course_id = new ArrayList<>();
        Cursor courseCursor = null;
        if(term_id.size() != 0)
            courseCursor = db.getCourses(term_id.get(term.getSelectedItemPosition()));
        if (courseCursor != null) {
            courseCursor.moveToFirst();
            while (!courseCursor.isAfterLast()) {
                course_id.add(courseCursor.getInt(courseCursor.getColumnIndex("_id")));
                courseCursor.moveToNext();
            }
        }

        //For each course, add all timetable hours to listview items
        for (int x  = 0; x < course_id.size(); x++){

            //Get list of class hours
            Cursor classCursor = db.getTimeByDay(course_id.get(x), 0, currentDay);
            Log.d("NOTE", "Classes:");
            classCursor.moveToFirst();
            while (!classCursor.isAfterLast() && classCursor.getCount() >= 1) {
                String[] p_id = new String[1];
                String parent;
                String type;
                String time;
                String start_time;
                String end_time;
                String location;
                String currentItem;
                String currentSubItem;

                int pid_row = classCursor.getColumnIndex("PARENT_ID");
                p_id[0] = Integer.toString(classCursor.getInt(pid_row));
                Cursor parentCursor = db.helper.getReadableDatabase().rawQuery("SELECT * FROM COURSE WHERE _ID =" + p_id[0], null);
                parentCursor.moveToFirst();
                parent = parentCursor.getString(parentCursor.getColumnIndex("COURSE_CODE"));

                type = "Class";

                start_time = classCursor.getString(classCursor.getColumnIndex("START_TIME"));
                end_time = classCursor.getString(classCursor.getColumnIndex("END_TIME"));
                time = start_time + " to " + end_time;

                location = classCursor.getString(classCursor.getColumnIndex("LOCATION"));

                currentItem = type + ": " + parent;
                item.add(currentItem);

                currentSubItem = time + ", " + location;
                subItem.add(currentSubItem);
                itemTime.add(time);

                classCursor.moveToNext();
            }
            classCursor.close();
            Log.d("NOTE", "Labs:");
            //Get list of lab/tut hours
            Cursor labCursor = db.getTimeByDay(course_id.get(x), 1, currentDay);
            labCursor.moveToFirst();
            while (!labCursor.isAfterLast()) {
                String[] p_id = new String[1];
                String parent;
                String type;
                String time;
                String start_time;
                String end_time;
                String location;
                String currentItem;
                String currentSubItem;

                int pid_row = labCursor.getColumnIndex("PARENT_ID");
                p_id[0] = Integer.toString(labCursor.getInt(pid_row));
                Cursor parentCursor = db.helper.getReadableDatabase().rawQuery("SELECT COURSE_CODE FROM COURSE WHERE _id =?", p_id);
                parentCursor.moveToFirst();
                parent = parentCursor.getString(parentCursor.getColumnIndex("COURSE_CODE"));

                type = "Lab/Tutorial";

                start_time = labCursor.getString(labCursor.getColumnIndex("START_TIME"));
                end_time = labCursor.getString(labCursor.getColumnIndex("END_TIME"));
                time = start_time + " to " + end_time;

                location = labCursor.getString(labCursor.getColumnIndex("LOCATION"));

                currentItem = type + ": " + parent;
                item.add(currentItem);

                currentSubItem = time + ", " + location;
                subItem.add(currentSubItem);
                itemTime.add(time);

                labCursor.moveToNext();
            }
            labCursor.close();

            Log.d("NOTE", "Office Hours");
            //Get list of office hours for all course instructors
            Cursor instructorCursor = db.getInstructors(course_id.get(x));
            ArrayList<Integer> instructor_id = new ArrayList<>();
            instructorCursor.moveToFirst();
            int i_id_col = instructorCursor.getColumnIndex("_id");
            while (!instructorCursor.isAfterLast()) {
                instructor_id.add(instructorCursor.getInt(i_id_col));
                instructorCursor.moveToNext();
            }
            Log.d("NOTE", "FOR LOOP");
            for(int y = 0; y < instructor_id.size(); y++) {
                Cursor officeCursor = db.getTimeByDay(instructor_id.get(y), 2, currentDay);
                officeCursor.moveToFirst();
                while (!officeCursor.isAfterLast()) {
                    String[] p_id = new String[1];
                    String parent;
                    String type;
                    String time;
                    String start_time;
                    String end_time;
                    String location;
                    String currentItem;
                    String currentSubItem;

                    int pid_row = officeCursor.getColumnIndex("PARENT_ID");
                    p_id[0] = Integer.toString(officeCursor.getInt(pid_row));
                    Cursor parentCursor = db.helper.getReadableDatabase().rawQuery("SELECT INAME FROM INSTRUCTOR WHERE _ID =?", p_id);
                    parentCursor.moveToFirst();
                    parent = parentCursor.getString(parentCursor.getColumnIndex("INAME"));

                    type = "Office Hours";

                    start_time = officeCursor.getString(officeCursor.getColumnIndex("START_TIME"));
                    end_time = officeCursor.getString(officeCursor.getColumnIndex("END_TIME"));
                    time = start_time + " to " + end_time;

                    location = officeCursor.getString(officeCursor.getColumnIndex("LOCATION"));

                    currentItem = type + ": " + parent;
                    item.add(currentItem);

                    currentSubItem = time + ", " + location;
                    subItem.add(currentSubItem);
                    itemTime.add(time);

                    officeCursor.moveToNext();
                }
            }
            Log.d("NOTE", "Misc. Hours");
            //Get list of misc hours for course
            Cursor miscCursor = db.getTimeByDay(course_id.get(x), 3, currentDay);
            miscCursor.moveToFirst();
            while (!miscCursor.isAfterLast()) {
                String[] p_id = new String[1];
                String parent;
                String type;
                String time;
                String start_time;
                String end_time;
                String location;
                String currentItem;
                String currentSubItem;

                int pid_row = miscCursor.getColumnIndex("PARENT_ID");
                p_id[0] = Integer.toString(miscCursor.getInt(pid_row));
                Cursor parentCursor = db.helper.getReadableDatabase().rawQuery("SELECT COURSE_CODE FROM COURSE WHERE _ID =?", p_id);
                parentCursor.moveToFirst();
                parent = parentCursor.getString(parentCursor.getColumnIndex("COURSE_CODE"));

                type = "Misc.";

                start_time = miscCursor.getString(miscCursor.getColumnIndex("START_TIME"));
                end_time = miscCursor.getString(miscCursor.getColumnIndex("END_TIME"));
                time = start_time + " to " + end_time;

                location = miscCursor.getString(miscCursor.getColumnIndex("LOCATION"));

                currentItem = type + ": " + parent;
                item.add(currentItem);

                currentSubItem = time + ", " + location;
                subItem.add(currentSubItem);
                itemTime.add(time);

                miscCursor.moveToNext();
            }
        }
        Log.d("NOTE", "Quicksort");
        //QuickSort listview items by time of occurrence
        if (!itemTime.isEmpty()) quickSort(0, itemTime.size() - 1);

        List<HashMap<String, String>> listText = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listText, R.layout.item_list,
                new String[]{"First", "Second"},
                new int[]{R.id.text1, R.id.text2});


        for(int h = 0; h < itemTime.size(); h++){
            HashMap<String, String> itemsMap = new HashMap<>();
            itemsMap.put("First", item.get(h));
            itemsMap.put("Second", subItem.get(h));
            listText.add(itemsMap);
        }



        Log.d("NOTE", "Adapter Set");
        for(int z = 0; z < item.size(); z++){
            Log.d("NOTE", itemTime.get(z) + "\n");
        }
        list.setAdapter(adapter);
        Log.d("NOTE", "Adapter Set");
    }

    //QuickSort
    private void quickSort(int high, int low){
        int i = low, j = high;
        String pivot = itemTime.get(low + (high - low)/2);
        while(i <= j){
            while(itemTime.get(i).compareTo(pivot) < 0){
                i++;
            }
            while(itemTime.get(i).compareTo(pivot) > 0){
                j--;
            }
            if (i <= j){
                String tempTime = itemTime.get(i);
                String tempItem = item.get(i);
                String tempSub = subItem.get(i);

                itemTime.set(i, itemTime.get(j));
                itemTime.set(j, tempTime);

                item.set(i, item.get(j));
                item.set(j, tempItem);

                subItem.set(i, subItem.get(j));
                subItem.set(j, tempSub);
                i++;
                j--;
            }
        }
        if (low < j) quickSort(low, j);
        if (i < high) quickSort(i, high);
    }
}
