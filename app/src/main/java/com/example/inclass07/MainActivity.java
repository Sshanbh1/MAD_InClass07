package com.example.inclass07;

/*
*
* Name : Sameer Shanbhag
* Name : Ravina Gaikawad
* Groups1 5
* In Class Assignment 07
*
* */




import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetTrackAsyncTask.IGetTrackAsyncTask {


    ArrayList<TrackDetails> localTrackDetails;


    Button bt_search;
    SeekBar sb_limit;
    EditText et_search;
    RadioGroup rg_rating;
    RadioButton rb_rating;
    RadioButton rb_artistrating;

    ProgressBar pb_loading;

    ListView lv_searchresults;

    TextView tv_display;

    int seekbarValue = 5;
    int radioSelect = 1; //1 for Track 2 for Artist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("MusixMatch Track Search");

        pb_loading = findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.GONE);

        tv_display = findViewById(R.id.tv_display);
        bt_search = findViewById(R.id.bt_search);
        et_search = findViewById(R.id.et_search);
        sb_limit = findViewById(R.id.sb_limit);
        rg_rating = findViewById(R.id.rg_rating);
        rb_rating = findViewById(R.id.rb_rating);
        rb_artistrating = findViewById(R.id.rb_artistrating);
        lv_searchresults = findViewById(R.id.lv_searchresults);

        sb_limit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                tv_display.setText("Limit: "+ progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbarValue = progressChangedValue;
            }
        });

        rg_rating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.rb_rating) {
                    radioSelect = 1;
                    pb_loading.setVisibility(View.VISIBLE);
                    changeContent();
                    pb_loading.setVisibility(View.GONE);
                } else if(checkedId == R.id.rb_artistrating) {
                    radioSelect = 2;
                    pb_loading.setVisibility(View.VISIBLE);
                    changeContent();
                    pb_loading.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MainActivity.this, "Select a Value", Toast.LENGTH_SHORT).show();
                }
            }

        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContent();
            }
        });
    }

    private void changeContent(){
        pb_loading.setVisibility(View.VISIBLE);
        if(isConnected()) {
            RequestParams requestParams = new RequestParams();
            requestParams.addParameter("q", et_search.getText().toString());
            requestParams.addParameter("page_size", Integer.toString(seekbarValue));
            if (radioSelect == 1) {
                requestParams.addParameter("s_track_rating", "desc");
            } else {
                requestParams.addParameter("s_artist_rating", "desc");
            }

            new GetTrackAsyncTask(requestParams, MainActivity.this).execute("http://api.musixmatch.com/ws/1.1/track.search");

        } else {
            Toast.makeText(MainActivity.this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
        }
        pb_loading.setVisibility(View.GONE);
    }

    private boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if(networkCapabilities != null)
        {
            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            {
                return true;
            }
        }
        return false;
    }


    @Override
    public void getTrackArrayList(final ArrayList<TrackDetails> trackDetailsArrayList) {
        this.localTrackDetails = trackDetailsArrayList;
        if(this.localTrackDetails.size() > 0){
            TrackArrayAdapter adapter = new TrackArrayAdapter(MainActivity.this, this.localTrackDetails);
            lv_searchresults.setAdapter(adapter);
            lv_searchresults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = localTrackDetails.get(position).getTrack_share_url();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        } else {
            Toast.makeText(this, "No Songs to View !!", Toast.LENGTH_SHORT).show();
        }
    }
}
