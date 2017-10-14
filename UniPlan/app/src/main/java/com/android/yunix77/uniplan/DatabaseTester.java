package com.android.yunix77.uniplan;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.util.ArrayList;

public class DatabaseTester {

    private DatabaseControl control;

    public DatabaseTester(DatabaseControl control) {
        ArrayList<String> res = new ArrayList<String>();
        this.control          = control;
        res                   = testAdd(res);
        res                   = testGet(res);

        printLog(res);
    }

    private ArrayList<String> testAdd(ArrayList<String> res) {
        res.add("Testing addTerm...");
        res.add("================START===============");
        res.add(control.addTerm(2017, 1, "2017-01-01", "2017-02-02"));
        res.add("================END=================");
        res.add("Testing addCourse...");
        res.add("================START===============");
        res.add(control.addCourse(1, "DB123", "TEST COURSE"));
        res.add(control.addCourse(2, "DB123", "TEST COURSE"));
        res.add("================END=================");
        res.add("Testing addInstructor...");
        res.add("================START===============");
        res.add(control.addInstructor("WATSON", 1, 0, null));
        res.add("================END=================");
        res.add("Testing addEvent...");
        res.add("================START===============");
        res.add(control.addEvent(1, "TEST_EVENT", 0, 0, 0, "2017-09-09", "01:00:00", "00:00:00", null, "RM2017"));
        res.add("================END=================");
        res.add("Testing addTime...");
        res.add("================START===============");
        res.add(control.addTime(1, 0, "LOC", "NOTE", "11:11:11", "00:00:00", 1));
        res.add("================END=================");
        return res;
    }

    private ArrayList<String> testGet(ArrayList<String> res) {
        res.add("Testing getTerms...");
        res.add("================START===============");
        res = cursorToArr(control.getTerms(), res);
        res.add("================END=================");
        res.add("Testing getCourse...");
        res.add("================START===============");
        res = cursorToArr(control.getCourses(1), res);
        res.add("================END=================");
        res.add("Testing getInstructor...");
        res.add("================START===============");
        res = cursorToArr(control.getInstructors(1), res);
        res.add("================END=================");
        res.add("Testing getEvent...");
        res.add("================START===============");
        res = cursorToArr(control.getEvents(1), res);
        res.add("================END=================");

        return res;
    }

    private ArrayList<String> cursorToArr(Cursor cursor, ArrayList<String> res) {
        if (cursor != null) {
            int startPosition = cursor.getPosition();
            cursor.moveToFirst();
            do {
                String[] columnNames = cursor.getColumnNames();
                int length = columnNames.length;
                for (int i = 0; i < length; i++) {
                    String value;
                    try {
                        value = cursor.getString(i);
                    } catch (SQLiteException e) {
                        value = "<unprintable>";
                    }
                    res.add(columnNames[i] + " : " + value);
                }
            } while (cursor.moveToNext());
            cursor.moveToPosition(startPosition);
        }
        return res;
    }

    private void printLog(ArrayList<String> res) {
        for(int i = 0; i < res.size(); i ++)
            Log.i("TEST_RESULT", res.get(i));
    }
}
