package com.android.yunix77.uniplan;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import com.android.yunix77.uniplan.RVAdapter_home;
import com.android.yunix77.uniplan.EventData;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {

    View                       view;
    List<EventData>            recentEvents;
    StaggeredGridLayoutManager layout;
    RecyclerView               rv;
    RVAdapter_home             rvAdapterHome;
    Context                    context;
    appTitleInterface          appTitleInterface;

    private ProgressDialog     progressDialog;

    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance(){
        return new homeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context            = context;
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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appTitleInterface.onSetTitle("Home");
    }

    @Override
    public void onPause() {
        super.onPause();
        progressDialog = null;
    }

    private void setUpRecyclerView() {
        rv            = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        rvAdapterHome = new RVAdapter_home(recentEvents);

        rv.setHasFixedSize(true);

        rvAdapterHome.setListener(new RVAdapter_home.Listener() {
            @Override
            public void onClick(EventData data) {
                Intent intent = new Intent(getActivity(),EventDetailsActivity.class);

                getActivity().startActivity(intent);
            }
        });

        rv.setAdapter(rvAdapterHome);

        layout = new StaggeredGridLayoutManager(2, 1);

        rv.setLayoutManager(layout);
    }

    public interface homeFragmentListener{
    }

}
