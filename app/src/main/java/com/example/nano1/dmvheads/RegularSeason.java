package com.example.nano1.dmvheads;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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
    private static final String URL = "http://dmvfootballheads.x10host.com/webservice/reg_season_";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_POS = "pos";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_TEAM_NAME = "team_name";
    private static final String TAG_WINS = "wins";
    private static final String TAG_LOSSES = "losses";
    private static final String TAG_TIES = "ties";
    private static final String TAG_AGAINST = "points_against";
    private static final String TAG_FOR = "points_for";
    //it's important to note that the message is both in the parent branch of
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.


    //An array of all of our comments
    private JSONArray mRegSeasons = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mRegSeasonList;
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
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setSubtitle(nYear);
        lv1 = (ListView) findViewById(R.id.listView2);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadTable().execute();
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

        //Instantiate the arraylist to contain all the JSON data.
        //we are going to use a bunch of key-value pairs, referring
        //to the json element name, and the content, for example,
        //message it the tag, and "I'm awesome as the content..

        mRegSeasonList = new ArrayList<HashMap<String, String>>();

        //Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        //Feed the beast out comments url, and it spits us
        //back a JSON object. Boo-yea Jerome.
        JSONObject json = jParser.getJSONFromUrl(REG_SEASON_URL);

        //when parsing JSON stuff, we should probably try to catch any exceptions:
        try {
            //I know I said we would check if "Posts were Avail."(success==1)
            //before we tried to read the individual posts, but I lied...
            //mComments will tell us how many "posts" or comments are available
            mRegSeasons = json.getJSONArray(TAG_POSTS);


            //looping through all posts according to the json object returned
            for (int i = 0; i < mRegSeasons.length(); i++) {
                JSONObject c = mRegSeasons.getJSONObject(i);

                //gets the content of each tag
                String position = c.getString(TAG_POS);
                String team_name = c.getString(TAG_TEAM_NAME);
                String wins = c.getString(TAG_WINS );
                String losses = c.getString(TAG_LOSSES);
                String ties = c.getString(TAG_TIES);
                String p_for = c.getString(TAG_FOR);
                String p_against = c.getString(TAG_AGAINST);

                //creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_POS, position);
                map.put(TAG_TEAM_NAME, team_name);
                map.put(TAG_WINS, wins);
                map.put(TAG_LOSSES, losses);
                map.put(TAG_TIES, ties);
                map.put(TAG_FOR, p_for);
                map.put(TAG_AGAINST, p_against);

                //adding HashList to ArrayList
                mRegSeasonList.add(map);

                //Annnnnddd, our JSON data is up to data same with our array list

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
        ListAdapter adapter2 = new SimpleAdapter(this, mRegSeasonList, R.layout.single_row,
                new String[] {TAG_POS,TAG_TEAM_NAME, TAG_WINS, TAG_LOSSES, TAG_TIES, TAG_FOR, TAG_AGAINST},
                new int[] {R.id.txtPos, R.id.txtName, R.id.txtW, R.id.txtL, R.id.txtT, R.id.txtFor,R.id.txtAgainst});

        //I shouldn't have to comment on this one:
        lv1.setAdapter(adapter2);

        //Optional: when the user clicks a list item we could do something.
        //However, we will choose to do nothing...

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //This method is triggered if an item is click whithin our list.
                //For our example we won't be using this, but it is useful to know in
                //real life applications.
            }
        });

    }

    public class LoadTable extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegularSeason.this);
            pDialog.setMessage("Loading Table...");
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
