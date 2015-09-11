package jamstechs.com.gayathrijapamtracker;

// Created by sudhakar on 8/24/2015.

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AddTransactions extends Activity implements OnClickListener {

    private EditText weekending, count;
    private Button mSubmit;
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String POST_COUNT_URL = "http://sageofkanchi.divinetraveller.net/addtransactions2.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaycounter);
        //setup input fields
        count = (EditText) findViewById(R.id.edweekcount);
        weekending = (EditText) findViewById(R.id.edWeekend);
        //setup buttons
        mSubmit = (Button) findViewById(R.id.post_week_total);
        mSubmit.setText("Enter Total and Submit");
        //register listeners
        mSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        new PostComment().execute();
    }

    class PostComment extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddTransactions.this);
            pDialog.setMessage("Posting Counts...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // Check for success tag
            int success;
            String message;
            String post_count = count.getText().toString();
            String post_week = weekending.getText().toString();
            //Retrieving Saved Username Data:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AddTransactions.this);
            String post_username = sp.getString("username", "anon");

            try {
                // Building Parameters
                System.out.println("post user  ..." + post_username);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", post_username));
                params.add(new BasicNameValuePair("weekcnt", post_count));
                params.add(new BasicNameValuePair("weekend", post_week));
                Log.d("request!", "starting");
                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        POST_COUNT_URL, "POST", params);
                // full json response
                Log.d("Post Count attempt", json.toString());
                // json success element
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Log.d("Count Added!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Transaction Failure!", json.getString(TAG_MESSAGE));
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
            if (file_url != null) {
                Toast.makeText(AddTransactions.this, file_url, Toast.LENGTH_LONG).show();
            }
            mSubmit.setEnabled(false);
            mSubmit.setClickable(false);
            mSubmit.setBackgroundColor(Color.parseColor("#808080"));
            finish();

        }

    }


}
