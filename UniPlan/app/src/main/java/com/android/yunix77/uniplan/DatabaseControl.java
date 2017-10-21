package com.android.yunix77.uniplan;//Class used to modify database

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import java.lang.String;
import android.database.SQLException;
import android.database.Cursor;

public class DatabaseControl {
    DatabaseHelper helper;

    //Initialize database helper
    public DatabaseControl(Context context) {
        helper = new DatabaseHelper(context);
    }

    //Add functions
    //Add Term
    public String addTerm(int y, int s, String s_d, String e_d) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("YEAR", y);
        values.put("SEMESTER", s);
        values.put("START_DATE", s_d);
        values.put("END_DATE", e_d);

        //String to return
        String result;

        try {
            db.insertOrThrow("Term_Schedule", null, values);
            result = "Term added successfully";
        } catch (SQLException x) {
            result = x.getMessage();
        }
        return result;

    }

    //Add course
    public String addCourse(int t_i, String c, String n) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("TERM_ID", t_i);
        values.put("COURSE_CODE", c);
        values.put("CNAME", n);

        //String to return
        String result;

        try {
            db.insertOrThrow("Course", null, values);
            result = "Course added successfully";
        } catch (SQLException x) {
            result = x.getMessage();
        }
        return result;
    }

    //Add Instructor (TA or Prof)
    public String addInstructor(String n, int c_i, int s, String e) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("INAME", n);
        values.put("COURSE_ID", c_i);
        values.put("STATUS", s);
        values.put("EMAIL", e);

        //String to return
        String result;

        try {
            db.insertOrThrow("Instructor", null, values);
            result = "Instructor added successfully";
        } catch (SQLException x) {
            result = x.getMessage();
        }
        return result;
    }

    //Add Event (Item in Calendar)
    public String addEvent(int c_i, String en, int t, int w, int g, String d, String s_t, String e_t, String n, String l) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("COURSE_ID", c_i);
        values.put("ENAME", en);
        values.put("TYPE", t);
        values.put("WEIGHT", w);
        values.put("GRADE", g);
        values.put("DATE", d);
        values.put("START_TIME", s_t);
        values.put("END_TIME", e_t);
        values.put("NOTE", n);
        values.put("LOCATION", l);

        //String to return
        String result;

        try {
            db.insertOrThrow("Event", null, values);
            result = "Calendar Event added successfully";
        } catch (SQLException x) {
            result = x.getMessage();
        }
        return result;
    }

    //Add Timetable item
    public String addTime(int p_i, int t, String l, String n, String s_t, String e_t, int d) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("PARENT_ID", p_i);
        values.put("TYPE", t);
        values.put("LOCATION", l);
        values.put("NOTE", n);
        values.put("START_TIME", s_t);
        values.put("END_TIME", e_t);
        values.put("DAY", d);

        //String to return
        String result;

        try {
            db.insertOrThrow("Time", null, values);
            result = "Timetable Event added successfully";
        } catch (SQLException x) {
            result = x.getMessage();
        }
        return result;
    }

    //Get functions: Retrieve tuples
    //Get Terms
    public Cursor getTerms() {
        //Database in read mode
        SQLiteDatabase db = helper.getReadableDatabase();

        //Query full Term table
        /*Cursor c = db.query(
                "Term_Schedule", //Query Term table
                null,   //Query all columns
                null,   //Query all rows
                null,   //No WHERE clause
                null,   //No args for WHERE
                null,   //No grouping
                null,   //No HAVING clause
                null   //Order by start date
        );*/
        Cursor c = db.rawQuery("select * from Term_Schedule", null);
        return c;
    }

    //Get Course by Term ID
    public Cursor getCourses(int t_id) {
        //DB in read mode
        SQLiteDatabase db = helper.getReadableDatabase();

        //Arguments for query
        String[] args = {Integer.toString(t_id)};
        //Query Course table by Term ID
       /* Cursor c = db.query(
                "Course",       //Query Course table
                null,           //Query all columns
                "TERM_ID = ?",  //Query rows by TERM_ID
                args,           //Query by inputted TERM_ID
                null,           //No grouping
                null,           //No HAVING clause
                null            //No ordering
        );*/
        Cursor c = db.rawQuery("SELECT * FROM Course WHERE TERM_ID=?", args);
        return c;
    }

    //Get Instructors by Course
    public Cursor getInstructors(int c_id) {
        //DB in read mode
        SQLiteDatabase db = helper.getReadableDatabase();

        //Arguments for query
        String[] args = {Integer.toString(c_id)};
        //Query Instructor table by Course ID
       /* Cursor c = db.query(
                "Instructor",       //Query Course table
                null,           //Query all columns
                "COURSE_ID = ?",  //Query rows by COURSE_ID
                args,           //Query by inputted COURSE_ID
                null,           //No grouping
                null,           //No HAVING clause
                "INAME"         //Order by name
        );*/

        Cursor c = db.rawQuery("SELECT * FROM Instructor WHERE COURSE_ID=? ORDER BY INAME DESC", args);;
        return c;
    }

    //Get Events by Course
    public Cursor getEvents(int c_id) {
        //DB in read mode
        SQLiteDatabase db = helper.getReadableDatabase();

        //Arguments for query
        String[] args = {Integer.toString(c_id)};
        //Query Course table by Term ID
       /* Cursor c = db.query(
                "EVENT",       //Query Course table
                null,           //Query all columns
                "COURSE_ID = ?",  //Query rows by COURSE_ID
                args,           //Query by inputted COURSE_ID
                null,           //No grouping
                null,           //No HAVING clause
                "DATE, START_TIME"            //Order by date and time
        );*/

        Cursor c = db.rawQuery("SELECT * FROM Event WHERE COURSE_ID=?", args);
        return c;
    }

    //Get Timetable items by Parent
    public Cursor getTime(int p_id, int type) {
        //DB in read mode
        SQLiteDatabase db = helper.getReadableDatabase();

        //Arguments for query
        String[] args = {Integer.toString(p_id), Integer.toString(type)};
        //Query Course table by Term ID
        Cursor c = db.query(
                "Time",         //Query Course table
                null,           //Query all columns
                "PARENT_ID = ? AND TYPE = ?",  //Query rows by parent id and type
                args,           //Query by inputted PARENT_ID and type
                null,           //No grouping
                null,           //No HAVING clause
                "DAY, START_TIME"            //Order by date and time
        );
        return c;
    }

    //Update functions
    //Update Term
    public void updateTerm(int t_id, int y, int s, String s_d, String e_d) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {Integer.toString(t_id)};

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("YEAR", y);
        values.put("SEMESTER", s);
        values.put("START_DATE", s_d);
        values.put("END_DATE", e_d);

        db.update("Term_Schedule", values, "WHERE TERM_ID = ?", args);
    }

    //Update course
    public void updateCourse(int c_id, int t_i, String c, String n) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {Integer.toString(c_id)};

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("TERM_ID", t_i);
        values.put("COURSE_CODE", c);
        values.put("CNAME", n);

        //Update selected course
        db.update("Course", values, "WHERE COURSE_ID = ?", args);
    }

    //Update Instructor (TA or Prof)
    public void updateInstructor(int i_id, String n, int c_i, int s, String e) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {Integer.toString(i_id)};

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("INAME", n);
        values.put("COURSE_ID", c_i);
        values.put("STATUS", s);
        values.put("EMAIL", e);

        //Update selected instructor
        db.update("Instructor", values, "WHERE Instructor_ID = ?", args);
    }

    //Update Event (Item in Calendar)
    public void updateEvent(int e_id, int c_i, String en, int t, int w, int g, String d, String s_t, String e_t, String n, String l) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {Integer.toString(e_id)};

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("COURSE_ID", c_i);
        values.put("ENAME", en);
        values.put("TYPE", t);
        values.put("WEIGHT", w);
        values.put("GRADE", g);
        values.put("DATE", d);
        values.put("START_TIME", s_t);
        values.put("END_TIME", e_t);
        values.put("NOTE", n);
        values.put("LOCATION", l);

        //Update selected calendar item
        db.update("Event", values, "WHERE EVENT_ID = ?", args);
    }

    //Update Timetable item
    public void updateTime(int t_id, int p_i, int t, String l, String n, String s_t, String e_t, int d) {
        //Database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {Integer.toString(t_id)};

        //Tuple to be inserted into db
        ContentValues values = new ContentValues();
        values.put("PARENT_ID", p_i);
        values.put("TYPE", t);
        values.put("LOCATION", l);
        values.put("NOTE", n);
        values.put("START_TIME", s_t);
        values.put("END_TIME", e_t);
        values.put("DAY", d);

        //Update selected timetable item
        db.update("Time", values, "WHERE TIME_ID = ?", args);
    }

    public String deleteTerm(int t_id) {
        SQLiteDatabase db  = helper.getWritableDatabase();
        String[] args      = {Integer.toString(t_id)};
        String query       = "DELETE FROM TERM_SCHEDULE WHERE TERM_ID=?";
        try{
            db.rawQuery(query,args).moveToFirst();
            return "Term Deleted Successfully";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    public String deleteCourse(int c_id) {
        SQLiteDatabase db  = helper.getWritableDatabase();
        String[] args      = {Integer.toString(c_id)};
        String query       = "DELETE FROM COURSE WHERE COURSE_ID=?";
        try{
            db.rawQuery(query,args).moveToFirst();
            return "Course Deleted Successfully";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    public String deleteInstructor(int i_id) {
        SQLiteDatabase db  = helper.getWritableDatabase();
        String[] args      = {Integer.toString(i_id)};
        String query       = "DELETE FROM INSTRUCTOR WHERE INSTRUCTOR_ID=?";
        try{
            db.rawQuery(query,args).moveToFirst();
            return "Instructor Deleted Successfully";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    public String deleteEvent(int e_id) {
        SQLiteDatabase db  = helper.getWritableDatabase();
        String[] args      = {Integer.toString(e_id)};
        String query       = "DELETE FROM EVENT WHERE EVENT_ID=?";
        try{
            db.rawQuery(query,args).moveToFirst();
            return "Event Deleted Successfully";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    public String deleteTime(int t_id) {
        SQLiteDatabase db  = helper.getWritableDatabase();
        String[] args      = {Integer.toString(t_id)};
        String query       = "DELETE FROM TIME WHERE TIME_ID=?";
        try{
            db.rawQuery(query,args).moveToFirst();
            return "Time Deleted Successfully";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    public String deleteAll() {
        SQLiteDatabase db  = helper.getWritableDatabase();
        try {
            db.delete("TERM_SCHEDULE", null, null);
            db.delete("COURSE", null, null);
            db.delete("INSTRUCTOR", null, null);
            db.delete("EVENT", null, null);
            db.delete("TIME", null, null);
            return "All entries Deleted Successfully";
        }catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}