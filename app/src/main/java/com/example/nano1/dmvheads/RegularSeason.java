package com.example.nano1.dmvheads;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RegularSeason extends AppCompatActivity {


    private static final String URL = "http://dmvfootballheads.x10host.com/webservice/reg_season_";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POS = "pos";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_TEAM_NAME = "team_name";
    private static final String TAG_WINS = "wins";
    private static final String TAG_LOSSES = "losses";
    private static final String TAG_TIES = "ties";
    private static final String TAG_AGAINST = "points_against";
    private static final String TAG_FOR = "points_for";
    private ArrayList<HashMap<String, String>> mRegSeasonList;
    JSONArray mRegSeasons = null;
    String nYear;
    String REG_SEASON_URL = null;
    ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_season);
        Bundle extras = getIntent().getExtras();
        nYear = extras.getString("year");
        REG_SEASON_URL = URL + nYear + ".php";

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setSubtitle(nYear);
        }
        lv1 = (ListView) findViewById(R.id.listView2);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadSeasonTable(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.playoffs:
                Toast.makeText(this, "coming Soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.statistics:
                Toast.makeText(this, "Stats Not Ready", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    /**
     * Retrieves recent post data from the server
     */
    public void updateJSONdata() {

        mRegSeasonList = new ArrayList<HashMap<String, String>>();
        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(REG_SEASON_URL);

        try {

            mRegSeasons = json.getJSONArray(TAG_POSTS);

            for (int i = 0; i < mRegSeasons.length(); i++) {
                JSONObject c = mRegSeasons.getJSONObject(i);

                String position = c.getString(TAG_POS);
                String team_name = c.getString(TAG_TEAM_NAME);
                String wins = c.getString(TAG_WINS );
                String losses = c.getString(TAG_LOSSES);
                String ties = c.getString(TAG_TIES);
                String p_for = c.getString(TAG_FOR);
                String p_against = c.getString(TAG_AGAINST);

                HashMap<String, String> map = new HashMap<>();
                map.put(TAG_POS, position);
                map.put(TAG_TEAM_NAME, team_name);
                map.put(TAG_WINS, wins);
                map.put(TAG_LOSSES, losses);
                map.put(TAG_TIES, ties);
                map.put(TAG_FOR, p_for);
                map.put(TAG_AGAINST, p_against);

                mRegSeasonList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into our listview
     */
    public void updateList() {
        ListAdapter adapter2 = new SimpleAdapter(this, mRegSeasonList, R.layout.single_row,
                new String[] {TAG_POS,TAG_TEAM_NAME, TAG_WINS, TAG_LOSSES, TAG_TIES, TAG_FOR, TAG_AGAINST},
                new int[] {R.id.txtPos, R.id.txtName, R.id.txtW, R.id.txtL, R.id.txtT, R.id.txtFor,R.id.txtAgainst});

        lv1.setAdapter(adapter2);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

}
