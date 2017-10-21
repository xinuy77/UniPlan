package com.android.yunix77.uniplan;//Class used to construct and access database

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UniPlan.db";

    //Create database and tables if they do not exist already
    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //context.deleteDatabase("UniPlan.db");
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Term_Schedule ("
                + "TERM_ID INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
                + "YEAR INT,"
                + "SEMESTER INT,"
                + "START_DATE VARCHAR,"
                + "END_DATE VARCHAR);");
        //Course
        db.execSQL("CREATE TABLE IF NOT EXISTS Course("
                + "COURSE_ID INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
                + "TERM_ID INT,"
                + "COURSE_CODE VARCHAR,"
                + "CNAME VARCHAR"
                + "CURRENT_GRADE INT DEFAULT 0,"
                + "FOREIGN KEY(TERM_ID) REFERENCES TERM_SCHEDULE(TERM_ID)"
                + "ON UPDATE CASCADE ON DELETE CASCADE);");
        //Instructor (i.e. TAs and Profs)
        db.execSQL("CREATE TABLE IF NOT EXISTS Instructor("
                + "INSTRUCTOR_ID INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
                + "INAME VARCHAR INT,"
                + "COURSE_ID INT NOT NULL,"
                + "STATUS INT NOT NULL,"
                + "EMAIL VARCHAR," +
                " FOREIGN KEY(COURSE_ID) REFERENCES COURSE(COURSE_ID)" +
                " ON UPDATE CASCADE ON DELETE CASCADE);");  //Note 0 = prof, 1 = TA
        //Event (i.e. items on calendar)
        db.execSQL("CREATE TABLE IF NOT EXISTS Event("
                + "EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
                + "COURSE_ID INT NOT NULL,"
                + "ENAME VARCHAR NOT NULL,"
                + "TYPE INT NOT NULL,"
                + "WEIGHT INT,"
                + "GRADE INT,"
                + "DATE VARCHAR,"
                + "START_TIME VARCHAR,"
                + "END_TIME VARCHAR,"
                + "NOTE VARCHAR,"
                + "LOCATION VARCHAR," +
                " FOREIGN KEY(COURSE_ID) REFERENCES COURSE(COURSE_ID)" +
                " ON UPDATE CASCADE ON DELETE CASCADE);");   //0 = test, 1 = exam, 2 = quiz, 3 = assignment, 4 = misc
        //Time (i.e. items on weekly timetable)
        db.execSQL("CREATE TABLE IF NOT EXISTS Time("
                + "TIME_ID INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
                + "PARENT_ID INT NOT NULL,"
                + "TYPE INT NOT NULL,"
                + "LOCATION VARCHAR,"
                + "NOTE VARCHAR,"
                + "START_TIME VARCHAR,"
                + "END_TIME VARCHAR,"
                + "DAY INT);");   //0 = class, 1 = lab/tut, 2 = office hours, 3 = misc.
    }
    //If database is reconfigured, erase data in table
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("");
        onCreate(db);
    }
}
