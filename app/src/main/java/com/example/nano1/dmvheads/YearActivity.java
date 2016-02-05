package com.example.nano1.dmvheads;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class YearActivity extends AppCompatActivity {

    private static final String READ_COMMENTS_URL = "http://dmvfootballheads.x10host.com/webservice/seasons.php";
    private static final String TAG_YEAR = "year";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_FIRST = "first_place";
    private static final String TAG_SECOND = "second_place";
    private static final String TAG_THIRD = "third_place";
    private ArrayList<Year> mSeasonList;
    JSONArray mSeasons = null;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_year);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

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
        mSeasonList = new ArrayList<>();
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

                Year mYear = new Year(year,first_p,second_p,third_p);
                mSeasonList.add(mYear);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into our RecyclerView
     */
    public void updateList() {
        final YearAdapter adapter = new YearAdapter(mSeasonList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String year = adapter.mArrayOfYears.get(position).year;
                Intent intent = new Intent(getBaseContext(), RegularSeasonActivity.class);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        }));
    }

}
