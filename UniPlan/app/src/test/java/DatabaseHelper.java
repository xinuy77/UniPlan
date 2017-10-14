//Class used to construct and access database

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UniPlan.db";

    //Create database and tables if they do not exist already
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Term_Schedule("
                + "TERM_ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT DEFAULT 0,"
                + "YEAR INT(4),"
                + "SEMESTER INT(1),"
                + "START_DATE VARCHAR(10),"
                + "END_DATE VARCHAR(10));");
        //Course
        db.execSQL("CREATE TABLE IF NOT EXISTS Course("
                + "COURSE_ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT DEFAULT 0,"
                + "TERM_ID INT(11) NOT NULL,"
                + "COURSE_CODE VARCHAR(8),"
                + "CNAME VARCHAR(32)"
                + "CURRENT_GRADE INT(3) DEFAULT 0);");
        //Instructor (i.e. TAs and Profs)
        db.execSQL("CREATE TABLE IF NOT EXISTS Instructor("
                + "INSTRUCTOR_ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT DEFAULT 0,"
                + "INAME VARCHAR INT(32),"
                + "COURSE_ID INT(11) NOT NULL,"
                + "STATUS INT(1) NOT NULL,"
                + "EMAIL VARCHAR(32),"
                + "WHERE STATUS = 0 OR STATUS = 1);");  //Note 0 = prof, 1 = TA
        //Event (i.e. items on calendar)
        db.execSQL("CREATE TABLE IF NOT EXISTS Event("
                + "EVENT_ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT DEFAULT 0,"
                + "COURSE_ID INT(11) NOT NULL,"
                + "ENAME VARCHAR(16) NOT NULL,"
                + "TYPE INT(1) NOT NULL,"
                + "WEIGHT INT(11),"
                + "GRADE INT(11),"
                + "DATE VARCHAR(10)"
                + "START_TIME VARCHAR(5),"
                + "END_TIME VARCHAR(5),"
                + "NOTE VARCHAR(32),"
                + "LOCATION VARCHAR(16)"
                + "WHERE TYPE >= 0 AND TYPE <= 4);");   //0 = test, 1 = exam, 2 = quiz, 3 = assignment, 4 = misc
        //Time (i.e. items on weekly timetable)
        db.execSQL("CREATE TABLE IF NOT EXISTS Time("
                + "TIME_ID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT DEFAULT 0,"
                + "PARENT_ID INT(11) NOT NULL,"
                + "TYPE INT(1) NOT NULL,"
                + "LOCATION VARCHAR(16),"
                + "NOTE VARCHAR(32),"
                + "START_TIME VARCHAR(5),"
                + "END_TIME VARCHAR(5),"
                + "DAY INT(1)"
                + "WHERE TYPE >= 0 and TYPE <= 3);");   //0 = class, 1 = lab/tut, 2 = office hours, 3 = misc.
    }
    //If database is reconfigured, erase data in table
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("");
        onCreate(db);
    }
}
