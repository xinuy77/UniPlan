package com.android.yunix77.uniplan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;


/**
 * Created by Ra on 2017-10-24.
 */

public class AddEventFragment extends Fragment {
    //Database
    DatabaseControl db;

    //UI Components
    View myView;
    EditText addEName;
    Spinner addEventType;
    EditText addEventWeight;
    EditText addEventGrade;
    DatePicker addEventDate;
    TimePicker addEventStartTime;
    TimePicker addEventEndTime;
    EditText addEventLocation;
    EditText addEventNotes;
    Button submit;

    //Variables to be stored in DB
    int courseID;
    String eName;
    int type;
    int weight;
    int grade;
    String date;
        int day;
        int month;
        int year;
    String startTime;
        int startHour;
        int startMinute;
    String endTime;
        int endHour;
        int endMinute;
    String location;
    String notes;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_addevent, container, false);
        db = new DatabaseControl(getActivity());

        //Initialize components
        addEName = (EditText) myView.findViewById(R.id.addename);
        addEventType = (Spinner) myView.findViewById(R.id.addeventtype);
        addEventWeight = (EditText) myView.findViewById(R.id.addeventweight);
        addEventGrade = (EditText) myView.findViewById(R.id.addeventgrade);
        addEventDate = (DatePicker) myView.findViewById(R.id.addeventdate);
        addEventStartTime = (TimePicker) myView.findViewById(R.id.addeventstarttime);
        addEventEndTime = (TimePicker) myView.findViewById(R.id.addeventendtime);
        addEventLocation = (EditText) myView.findViewById(R.id.addeventlocation);
        addEventNotes = (EditText) myView.findViewById(R.id.addeventnotes);
        submit = (Button) myView.findViewById(R.id.submitEvent);

        courseID = this.getArguments().getInt("COURSE_ID");

        //Onclick "Add Term" button
        submit.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment
            public void onClick(View v){
                eName = addEName.getText().toString();
                type = addEventType.getSelectedItemPosition();
                if (addEventWeight.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getActivity(),"Please enter WEIGHT", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                weight = Integer.parseInt(addEventWeight.getText().toString());

                if (addEventGrade.getText().toString().isEmpty()){
                    grade = -1;
                }
                else {grade = Integer.parseInt(addEventGrade.getText().toString());}

                day = addEventDate.getDayOfMonth();
                month = addEventDate.getMonth() + 1;
                year = addEventDate.getYear();

                String dayString;
                if (day < 10){dayString = "0" + Integer.toString(day);}
                else{dayString = Integer.toString(day);}

                String monthString;
                if (month < 10){monthString = "0" + Integer.toString(month);}
                else{monthString = Integer.toString(month);}

                //Formate dates as "YYYY-MM-DD" strings
                String date = Integer.toString(year)
                        + "-"
                        + monthString
                        + "-"
                        + dayString;


                startHour = addEventStartTime.getCurrentHour();
                startMinute = addEventStartTime.getCurrentMinute();
                startTime = Integer.toString(startHour)
                        + ":"
                        + Integer.toString(startMinute);

                endHour = addEventEndTime.getCurrentHour();
                endMinute = addEventEndTime.getCurrentMinute();
                endTime = Integer.toString(endHour)
                        + ":"
                        + Integer.toString(endMinute);

                location = addEventLocation.getText().toString();
                notes = addEventNotes.getText().toString();

                if (eName.isEmpty()){
                    Toast toast = Toast.makeText(getActivity(),"Please enter NAME", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if ((weight < 0) || (weight > 100)){
                    Toast toast = Toast.makeText(getActivity(),"Please enter a valid WEIGHT", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (grade > 100){
                    Toast toast = Toast.makeText(getActivity(),"Please enter a valid GRADE", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (((startHour * 100) + startMinute)
                        > ((endHour * 100) + endMinute)){
                    Toast toast = Toast.makeText(getActivity(),"START TIME cannot be after END TIME", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if (location.isEmpty()){
                    Toast toast = Toast.makeText(getActivity(),"Please enter a LOCATION", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    if (getActivity().getCurrentFocus() != null) {
                        InputMethodManager inputManager =
                                (InputMethodManager) getActivity().
                                        getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //Add event to database
                    db.addEvent(courseID, eName, type, weight, grade, date, startTime, endTime, notes, location);
                    //Return to previous page
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();

                    //set event notification
                    setEventNotification();
                }
            }
        });
        return myView;
    }
    
    //set event timer notification
    private void setEventNotification () {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_menu_gallery);
        //builder.setTicker("This is a Ticker.");
        builder.setContentText("This is ContentText which should be replaced by some meaning data!");
        builder.setContentTitle("Title(repalce me pls)");
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //notification.setExtras()

        Intent intent = new Intent(getContext(), MainActivity.class);
        //intent.putExtra()
        PendingIntent activity = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(getContext(), com.android.yunix77.uniplan.NotificationPublisher.class);
        notificationIntent.putExtra(com.android.yunix77.uniplan.NotificationPublisher.NOTIFICATION_ID, com.android.yunix77.uniplan.NotificationPublisher.uniqueID);
        notificationIntent.putExtra(com.android.yunix77.uniplan.NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), com.android.yunix77.uniplan.NotificationPublisher.uniqueID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        //Alarm after 10s
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
    }
}
