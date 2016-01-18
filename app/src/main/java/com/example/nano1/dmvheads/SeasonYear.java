package com.example.nano1.dmvheads;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SeasonYear extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    //php read comments script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String READ_COMMENTS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";

    //testing on Emulator:
    //private static final String READ_COMMENTS_URL = "http://192.168.1.6:1337/webservice/comments.php";

    //testing from a real server:
    private static final String READ_COMMENTS_URL = "http://dmvfootballheads.x10host.com/webservice/seasons.php";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_YEAR = "year";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_NUM_TEAMS = "num_teams";
    private static final String TAG_FIRST = "first_place";
    private static final String TAG_SECOND = "second_place";
    private static final String TAG_THIRD = "third_place";
    //it's important to note that the message is both in the parent branch of
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.


    //An array of all of our comments
    private JSONArray mSeasons = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mSeasonList;
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
        new LoadComments().execute();
    }

    /**
     * Retrieves recent post data from the server
     */
    public void updateJSONdata() {

        //Instantiate the arraylist to contain all the JSON data.
        //we are going to use a bunch of key-value pairs, referring
        //to the json element name, and the content, for example,
        //message it the tag, and "I'm awesome as the content..

        mSeasonList = new ArrayList<HashMap<String, String>>();

        //Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        //Feed the beast out comments url, and it spits us
        //back a JSON object. Boo-yea Jerome.
        JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

        //when parsing JSON stuff, we should probably try to catch any exceptions:
        try {
            //I know I said we would check if "Posts were Avail."(success==1)
            //before we tried to read the individual posts, but I lied...
            //mComments will tell us how many "posts" or comments are available
            mSeasons = json.getJSONArray(TAG_POSTS);


            //looping through all posts according to the json object returned
            for (int i = 0; i < mSeasons.length(); i++) {
                JSONObject c = mSeasons.getJSONObject(i);

                //gets the content of each tag
                String year = c.getString(TAG_YEAR);
                String first_p = c.getString(TAG_FIRST);
                String second_p = c.getString(TAG_SECOND);
                String third_p = c.getString(TAG_THIRD);

                //creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_YEAR, year);
                map.put(TAG_FIRST, first_p);
                map.put(TAG_SECOND, second_p);
                map.put(TAG_THIRD, third_p);

                //adding HashList to ArrayList
                mSeasonList.add(map);

                //Annnnnddd, our JSON data is up to date same with our array list

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into our listview
     */
    private void updateList() {
        //For a ListActivity we need to set the List Adapter, and in order to do
        //that, we need to create a ListAdapter. This SimpleAdapter, weill utilize
        //our updated Hashmapped ArrayList, use our single_post xml template for each
        //iteam in our list, and place the appropriate info from the list to the
        //correct GUI id. Order is important here.
        ListAdapter adapter = new SimpleAdapter(this, mSeasonList, R.layout.single_post,
                new String[] {TAG_YEAR,TAG_FIRST, TAG_SECOND, TAG_THIRD},
                new int[] {R.id.year, R.id.first, R.id.second, R.id.third});

        //I shouldn't have to comment on this one:
        lv.setAdapter(adapter);

        //Optional: when the user clicks a list item we could do something.
        //However, we will choose to do nothing...

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //This method is triggered if an item is click whithin our list.
                //For our example we won't be using this, but it is useful to know in
                //real life applications.

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

    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SeasonYear.this);
            pDialog.setMessage("Loading History...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            //we will develop this method in version 2
            updateJSONdata();
            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            updateList();
        }
    }

}
