package com.android.yunix77.uniplan.Fragments_Discontinued;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import com.android.yunix77.uniplan.R;
import com.android.yunix77.uniplan.appTitleInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class placesFragment extends Fragment {

    View view;
    ProgressDialog progressDialog;
    JSONArray jsonArray;
    RecyclerView rv;
    RVAdapter_places rvAdapter_places;
    LinearLayoutManager layout;
    Context context;
    com.android.yunix77.uniplan.appTitleInterface appTitleInterface;

    public placesFragment() {
        // Required empty public constructor
    }

    public static placesFragment newInstance(){
        return new placesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        final Activity activity = getActivity();
        if (activity instanceof appTitleInterface) {
            appTitleInterface = (appTitleInterface) activity;
        } else {
            throw new IllegalArgumentException("Activity must implement appTitleInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_places, container, false);

        //progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        //If change page into place fragment, the program loops in "loading"
        //comment to skip this line till new implements

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appTitleInterface.onSetTitle("Places");
    }

    @Override
    public void onPause() {
        super.onPause();
        progressDialog = null;
    }

    private BroadcastReceiver onEventDataReceivedPlaces = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            view.invalidate();
            setUpView();
        }
    };

    private void setUpView() {
        rv = (RecyclerView) view.findViewById(R.id.recyclerView_places);
        rv.setHasFixedSize(true);
        rvAdapter_places = new RVAdapter_places(jsonArray);
        rvAdapter_places.setListener(new RVAdapter_places.Listener() {
            @Override
            public void onClick(String data) {
                makeTransactionToEventsFragment(data);
            }
        });
        rv.setAdapter(rvAdapter_places);
        layout = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layout);
    }

    private void makeTransactionToEventsFragment(String data) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public interface placesFragmentListener{
    }
}
