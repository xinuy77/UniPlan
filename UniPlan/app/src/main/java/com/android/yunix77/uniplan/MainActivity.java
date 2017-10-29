package com.android.yunix77.uniplan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        appTitleInterface{

    private DatabaseControl dbControl;
    private DatabaseTester  test;
                
    protected Fragment outlineFragment;
    protected Fragment calendarFragment;
    protected Fragment timetableFragment;
    protected Fragment gradesFragment;
    protected Fragment courseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outlineFragment    = new OutlineFragment();
        calendarFragment   = new CalendarFragment();
        timetableFragment  = new TimetableFragment();
        gradesFragment     = new GradesFragment();
        courseFragment     = new CourseFragment();
            
        dbControl = new DatabaseControl(getApplicationContext());
        //test      = new DatabaseTester(dbControl);
        initUI();
    }
                
    public void changeFragment(int id){
        Fragment currentFragment = null;

        if(id==R.id.nav_outline){
            currentFragment = outlineFragment;
        }
        else if(id==R.id.nav_course){
            currentFragment = courseFragment;
        }
        else if(id==R.id.nav_calendar){
            currentFragment = calendarFragment;
        }
        else if(id==R.id.nav_timetable){
            currentFragment = timetableFragment;
        }
        else if(id==R.id.nav_grades){
            currentFragment = gradesFragment;
        }

        if(currentFragment != null){
            FragmentManager fm     = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.include, currentFragment);
            ft.addToBackStack(null); //default is the previous one
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
    }

    public void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
            
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.include, outlineFragment);
        ft.commit();

        changeFragment(R.id.nav_outline);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        changeFragment(id);
            
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    
    @Override
    public void onSetTitle(String title) {
        setTitle(title);
    }
}
