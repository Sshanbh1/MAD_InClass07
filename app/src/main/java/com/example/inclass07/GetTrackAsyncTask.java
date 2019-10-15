package com.example.inclass07;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetTrackAsyncTask extends AsyncTask<String, Void, ArrayList<TrackDetails>> {
    RequestParams requestParams;
    IGetTrackAsyncTask getTrackAsyncTask;
    ArrayList<TrackDetails> result = new ArrayList<>();
    public GetTrackAsyncTask(RequestParams requestParams, IGetTrackAsyncTask getTrackAsyncTask) {
        this.requestParams = requestParams;
        this.getTrackAsyncTask = getTrackAsyncTask;
    }

    @Override
    protected ArrayList<TrackDetails> doInBackground(String... params) {

        HttpURLConnection connection = null;

        try {
            URL url = new URL(requestParams.getEncodedUrl(params[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                JSONObject root = new JSONObject(json);
                JSONObject message = root.getJSONObject("message");
                JSONObject body = message.getJSONObject("body");
                JSONArray trackList = body.getJSONArray("track_list");
                for (int i=0;i < trackList.length();i++) {
                    JSONObject getTrack = trackList.getJSONObject(i);
                    JSONObject track = getTrack.getJSONObject("track");
                    TrackDetails trackDetails = new TrackDetails();
                    Log.d("Bagh 1", track.toString());
                    trackDetails.setAlbum_name(track.getString("album_name"));
                    trackDetails.setArtist_name(track.getString("artist_name"));
                    trackDetails.setTrack_name(track.getString("track_name"));
                    trackDetails.setUpdated_time(track.getString("updated_time"));
                    trackDetails.setTrack_share_url(track.getString("track_share_url"));
                    result.add(trackDetails);
                }
            }
        } catch (Exception e) {
            Log.d("Bagh", e.toString());
        } finally {
            connection.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<TrackDetails> result) {
        if(result != null){
            getTrackAsyncTask.getTrackArrayList(result);
        }
    }

    public interface IGetTrackAsyncTask{
        void getTrackArrayList(ArrayList<TrackDetails> TrackArrayList);
    }
}