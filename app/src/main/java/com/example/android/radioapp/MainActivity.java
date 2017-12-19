package com.example.android.radioapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv_radio_list;
    RadioListAdapter adapter;
    NetworkImageView niv_current_station;
    LinearLayout ll_currently_playing;
    RadioItem current_radio_station = null;
    Intent serviceIntent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_radio_list = (RecyclerView) findViewById(R.id.rv_radio_list);
        rv_radio_list.setHasFixedSize(true);
        niv_current_station = (NetworkImageView) findViewById(R.id.niv_current_station);
        ll_currently_playing = (LinearLayout) findViewById(R.id.ll_current_play);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_radio_list.setLayoutManager(llm);
        rv_radio_list.setHasFixedSize(true);

        adapter = new RadioListAdapter(this);
        rv_radio_list.setAdapter(adapter);

        loadData();
    }

    private void loadData(){
        String url = getResources().getString(R.string.stations_url);

        JsonArrayRequest jsArrRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                radioListLoad(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Load Json Array","Error:"+ error.getMessage());
            }
        });

        ImageLoaderSingleton.getInstance(this).addToRequestQueue(jsArrRequest);
    }

    private void radioListLoad(JSONArray response){
        Log.d("RadioListLoad","OK");
        List<RadioItem> l = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject o = response.getJSONObject(i);
                String name = o.getString("name");
                String image_url = o.getString("image_url");
                String stream_url = o.getString("stream_url");
                String url = o.getString("url");

                RadioItem r = new RadioItem();

                r.setImageURL(image_url);
                r.setTitle(name);
                r.setUrl(url);
                r.setStream_url(stream_url);

                l.add(r);
            }
        }catch(JSONException e){
            Log.d("JSON Parser",e.getMessage());
            return;
        }
        populateRadioStationList(l);

    }

    private void populateRadioStationList(List<RadioItem> l){
        Log.d("Adapter","OK");
        adapter.updateData(l);
        adapter.notifyDataSetChanged();
    }

    public void changeRadio(RadioItem item){
        current_radio_station = item;
        update_current_station();
        Intent playIntent = new Intent(this, PlayActivity.class);
        playIntent.putExtra("item",item);
        startActivity(playIntent);

        if(serviceIntent!=null){
            stopService(serviceIntent);
        }
        serviceIntent = new Intent(this,MediaPlayerService.class);
        serviceIntent.putExtra("url",item.getStream_url());
        Log.d("Change URL",item.getStream_url());
        startService(serviceIntent);
    }

    private void update_current_station(){
        ll_currently_playing.setVisibility(View.VISIBLE);
        ImageLoader loader = ImageLoaderSingleton.getInstance(this).getImageLoader();
        niv_current_station.setImageUrl(current_radio_station.getImageURL(),loader);
    }
}
