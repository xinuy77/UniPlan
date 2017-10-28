package com.android.yunix77.uniplan.Fragments_Discontinued;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.yunix77.uniplan.R;

import org.json.JSONArray;
import org.json.JSONException;

public class RVAdapter_places extends RecyclerView.Adapter<RVAdapter_places.ViewHolder> {
    private Listener  listener;
            JSONArray jsonArray;

    public RVAdapter_places(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView view){
            super(view);
            cardView = view;
        }
    }

    @Override
    public RVAdapter_places.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_single_textview,parent,false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            createPlacesCard(holder, position);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    private void createPlacesCard(ViewHolder holder, final int position){
        //setup view
        CardView cardView = holder.cardView;
        TextView places   = (TextView)cardView.findViewById(R.id.card_single_textview);
        try {
            places.setText(jsonArray.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listener != null)
                    try {
                        listener.onClick(jsonArray.get(position).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    public interface Listener{
        void onClick(String data);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}
