package com.example.nano1.dmvheads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText user, pass;
    Button mSubmit;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static final String LOGIN_URL = "http://dmvfootballheads.x10host.com/webservice/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.username1);
        pass = (EditText)findViewById(R.id.password1);

        mSubmit = (Button)findViewById(R.id.login);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
                new AttemptLogin().execute();
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        String username;
        String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            username = user.getText().toString();
            password = pass.getText().toString();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;

            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());

                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.commit();

                    Intent i = new Intent(LoginActivity.this, YearActivity.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}