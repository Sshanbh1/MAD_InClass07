package com.example.inclass07;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TrackArrayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TrackDetails> trackDetails;

    public  TrackArrayAdapter(Context context, ArrayList<TrackDetails> trackDetails) {
        this.context = context;
        this.trackDetails = trackDetails;
    }

    @Override
    public int getCount() {
        return trackDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return trackDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.listview_custom, parent, false);
        }
        TextView albumName = convertView.findViewById(R.id.tv_AlbumName);
        TextView trackName = convertView.findViewById(R.id.tv_TrackName);
        TextView artistName = convertView.findViewById(R.id.tv_ArtistName);
        TextView updatedDate = convertView.findViewById(R.id.tv_UpdatedDate);

        albumName.setText(String.format("Album: %s", trackDetails.get(position).getAlbum_name()));
        trackName.setText(String.format("Track: %s", trackDetails.get(position).getTrack_name()));
        artistName.setText(String.format("Artist: %s", trackDetails.get(position).getArtist_name()));
        String convertDate = getDate(trackDetails.get(position).getUpdated_time());
        updatedDate.setText(String.format("Date: %s", convertDate));

        return convertView;
    }

    public String getDate(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

        Date data = null;
        try
        {
            data = input.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(data);

        return formatted;
    }
}
