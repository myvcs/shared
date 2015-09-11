package jamstechs.com.gayathrijapamtracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//Created by sudhakar on 9/1/2015.
public class ReadData extends Activity implements View.OnClickListener {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private int valGlCumcount = 0;
    private int valWcount, valOldcount = 0;
    private int valCumcount = 0;
    private String valWeek;
    private EditText weekending, count;
    String username = UserLogin.username2;
    private static final String READ_COUNTS_URL = "http://sageofkanchi.divinetraveller.net/getjapamcount.php";
    //JSON IDS:
    private static final String TAG_TITLE = "title";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_CCOUNT = "cumulative_count";
    private static final String TAG_WCOUNT = "weeks_count";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_GLOBALCOUNT = "global_counts";
    private static final String TAG_WEEKEND = "week_ended";
    private JSONArray mComments = null;
    private Button posttot;
   // private static final String POST_COUNT_URL = "http://sageofkanchi.divinetraveller.net/addtransactions2.php";
    private ArrayList<HashMap<String, String>> mCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadComments().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_week_total:
                new addComment();
                posttot = (Button) findViewById(R.id.post_week_total);
                posttot.setEnabled(false);
                posttot.setClickable(false);
                posttot.setBackgroundColor(Color.parseColor("#808080"));
                break;
            default:
                break;
        }
    }
    //Retrieves json data of counts
    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReadData.this);
            pDialog.setMessage("Loading data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            //Retrieving Saved Username Data:
            updateJSONdata();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            System.out.println("update list ");
            updateList();
        }
    }

     //Retrieves data from the server.
        public void updateJSONdata() {
        mCommentList = new ArrayList<HashMap<String, String>>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ReadData.this);
        String post_username = sp.getString("username", username);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", post_username));
        params.add(new BasicNameValuePair("password", post_username));
        //Posting user data to script
        //THE IMPORTANT PART I WAS STRUGGLING TO SEND DATA TO PHP ..WHEW...
        JSONObject json = jsonParser.makeHttpRequest(
                READ_COUNTS_URL, "POST", params);
        //try to catch any exceptions:
        try {
            mComments = json.getJSONArray(TAG_POSTS);
            // looping through all posts according to the json object returned
            for (int i = 0; i < mComments.length(); i++) {
                JSONObject c = mComments.getJSONObject(i);
                //gets the content of each tag
                String cumcount = c.getString(TAG_CCOUNT);
                valCumcount = Integer.parseInt(cumcount);
                String wcount = c.getString(TAG_WCOUNT);
                valWcount = Integer.parseInt(wcount);
                String username = c.getString(TAG_USERNAME);
                String gcount = c.getString(TAG_GLOBALCOUNT);
                valGlCumcount = Integer.parseInt(gcount);
                String weekend = c.getString(TAG_WEEKEND);
                valWeek = weekend;
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_CCOUNT, cumcount);
                if (map.containsKey(TAG_CCOUNT)) {
                    System.out.println("The hashmap contains " + cumcount);
                } else {
                    System.out.println("The hashmap does not contains key ");
                }
                map.put(TAG_WCOUNT, wcount);
                map.put(TAG_GLOBALCOUNT, gcount);
                map.put(TAG_USERNAME, username);
                map.put(TAG_WEEKEND, weekend);
                // adding HashList to ArrayList
                mCommentList.add(map);
                valOldcount = valWcount;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Inserts the parsed data into the fields.
    private void updateList() {
        TextView glcount, wklycount, micount, name, week, prevWeekcount, prevWeekend, mycumcount;
        setContentView(R.layout.displaycounter);
        posttot = (Button) findViewById(R.id.post_week_total);
        posttot.setClickable(true);
        posttot.setOnClickListener(this);
        name = (TextView) findViewById(R.id.username);
        name.setText(username);
        glcount = (TextView) findViewById(R.id.gcount);
        glcount.setText(String.valueOf(valGlCumcount));
        mycumcount = (TextView) findViewById(R.id.myCumcount);
        mycumcount.setText(String.valueOf(valCumcount));
        // micount =(TextView) findViewById(R.id.mycount);
        // micount.setText(String.valueOf(valWcount));
        // prevWeekcount=(TextView)findViewById(R.id.tvPrevWeekend);
        //prevWeekcount.setText(String.valueOf(valWcount));
        // week=(TextView)findViewById(R.id.tvPrevWeekend);
        //  week.setText(String.valueOf(valWeek));
        wklycount = (TextView) findViewById(R.id.edweekcount);
        wklycount.setText(String.valueOf(valWcount));
        wklycount.setEnabled(false);
        week = (TextView) findViewById(R.id.edWeekend);
        week.setText(String.valueOf(valWeek));
        week.setEnabled(false);
    }

    private class addComment {
        addComment() {
            Intent i = new Intent(ReadData.this, AddTransactions.class);
            startActivity(i);
        }
    }
}
