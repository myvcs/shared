package jamstechs.com.gayathrijapamtracker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//Created by sudhakar on 9/1/2015.
public class UserLogin extends Activity implements View.OnClickListener {
    private EditText user, pass;
    private Button mSubmit, mRegister;
    private int success=0;
    // Progress Dialog
    private ProgressDialog pDialog;
    static String username2 ;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://sageofkanchi.divinetraveller.net/login.php";
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ID = "userid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.userdata);

        //setup input fields
        user = (EditText)findViewById(R.id.edUser);
        pass = (EditText)findViewById(R.id.edPass);
        //setup buttons
        mSubmit = (Button)findViewById(R.id.buttonSignIn);
        mRegister = (Button)findViewById(R.id.buttonReg);
        //register listeners
        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);
      }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonSignIn:
                new AttemptLogin().execute();
                break;
            case R.id.buttonReg:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserLogin.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            Intent iLog = new Intent(UserLogin.this, Register.class);
            String username  = user.getText().toString();
            String password = pass.getText().toString();
            username2=username;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                System.out.println("params "+ params);
                Log.d("request!", "starting");
                // getting user details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);
                // check your log for json response
                Log.d("Login attempt", json.toString());
                // Async json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    // save user data
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(UserLogin.this);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.commit();
                    Intent i = new Intent(UserLogin.this, ReadData.class);
                   // finish();
                   startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
          protected void onPostExecute(String file_url) {
                   pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(UserLogin.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

}