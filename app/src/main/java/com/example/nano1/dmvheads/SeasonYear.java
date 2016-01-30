package com.example.nano1.dmvheads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SeasonYear extends AppCompatActivity {

    private static final String READ_COMMENTS_URL = "http://dmvfootballheads.x10host.com/webservice/seasons.php";
    private static final String TAG_YEAR = "year";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_FIRST = "first_place";
    private static final String TAG_SECOND = "second_place";
    private static final String TAG_THIRD = "third_place";
    private ArrayList<HashMap<String, String>> mSeasonList;
    JSONArray mSeasons = null;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_year);
        lv = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadSeason(this).execute();
    }

    /**
     * Retrieves recent post data from the server
     */
    public void updateJSONdata() {

        mSeasonList = new ArrayList<HashMap<String, String>>();
        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

        try {
            mSeasons = json.getJSONArray(TAG_POSTS);

            for (int i = 0; i < mSeasons.length(); i++) {
                JSONObject c = mSeasons.getJSONObject(i);

                String year = c.getString(TAG_YEAR);
                String first_p = c.getString(TAG_FIRST);
                String second_p = c.getString(TAG_SECOND);
                String third_p = c.getString(TAG_THIRD);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_YEAR, year);
                map.put(TAG_FIRST, first_p);
                map.put(TAG_SECOND, second_p);
                map.put(TAG_THIRD, third_p);

                mSeasonList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into our listview
     */
    public void updateList() {

        ListAdapter adapter = new SimpleAdapter(this, mSeasonList, R.layout.single_post,
                new String[] {TAG_YEAR,TAG_FIRST, TAG_SECOND, TAG_THIRD},
                new int[] {R.id.year, R.id.first, R.id.second, R.id.third});

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent w;
                switch (position) {
                    case 0:
                        w = new Intent(getBaseContext(), RegularSeason.class);
                        w.putExtra("year", "2015");
                        startActivity(w);
                        break;
                    case 1:
                        w = new Intent(getBaseContext(), RegularSeason.class);
                        w.putExtra("year", "2014");
                        startActivity(w);
                        break;
                    case 2:
                        w = new Intent(getBaseContext(), RegularSeason.class);
                        w.putExtra("year", "2013");
                        startActivity(w);
                        break;
                    case 3:
                        w = new Intent(getBaseContext(), RegularSeason.class);
                        w.putExtra("year", "2012");
                        startActivity(w);
                        break;
                    case 4:
                        w = new Intent(getBaseContext(), RegularSeason.class);
                        w.putExtra("year", "2011");
                        startActivity(w);
                        break;
                }
            }
        });
    }

}
