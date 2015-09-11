package jamstechs.com.gayathrijapamtracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sudhakar on 9/6/2015.
 */
public class SatsangRegister extends Activity implements View.OnClickListener {
    public static String SAT_NAME ;
    private Button mAddSang;
    private EditText satId, satnName, satConatctPer, satEmail, satMobile, satCity, satState, satCountry, satZip;
    private ProgressDialog pDialog;
    private static String satsangDesc;
    public static String satname;
    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String SATSANG_URL = "http://sageofkanchi.divinetraveller.net/register_sang.php";

    public static Object getVariable() {
        satsangDesc=SAT_NAME;
        return satsangDesc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize
        setContentView(R.layout.register_user_sang);
        mAddSang = (Button) findViewById(R.id.sangregister);
        satId = (EditText) findViewById(R.id.edSatId);
        satnName = (EditText) findViewById(R.id.satName);
        satConatctPer = (EditText) findViewById(R.id.satContactPerson);
        satEmail = (EditText) findViewById(R.id.satEmail);
        satMobile = (EditText) findViewById(R.id.satContactNo);
        satCity = (EditText) findViewById(R.id.satCity);
        satState = (EditText) findViewById(R.id.satState);
        satCountry = (EditText) findViewById(R.id.satCountry);
        satZip = (EditText) findViewById(R.id.satZip);
        mAddSang.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        new CreateSang().execute();


    }

    private class CreateSang extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SatsangRegister.this);
            pDialog.setMessage("Creating Satsang...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int success;
            satname="";
            //for debugging..can be removed later
            System.out.println("satsangparams..........  " + satname);
            //
            JSONParser jsonParser = new JSONParser();
            satname = satnName.getText().toString();
            String satcontact = satConatctPer.getText().toString();
            String email = satEmail.getText().toString();
            String mobile = satMobile.getText().toString();
            String city = satCity.getText().toString();
            String state = satState.getText().toString();
            String country = satCountry.getText().toString();
            String zip = satZip.getText().toString();

              //storing the value to take it back to calling activity Register..
                SharedPreferences sp = PreferenceManager
                     .getDefaultSharedPreferences(SatsangRegister.this);
              SharedPreferences.Editor edit = sp.edit();
            edit.putString("satname",satname);
            edit.commit();
            ////for debugging..can be removed later
                System.out.println("satsangparams..........  " + satname);

                //

            try {
                List<NameValuePair> satParams = new ArrayList<>();
                // satParams.add(new BasicNameValuePair("satid",satsangid));
                satParams.add(new BasicNameValuePair("satname", satname));
                satParams.add(new BasicNameValuePair("satcontact", satcontact));
                satParams.add(new BasicNameValuePair("email", email));
                satParams.add(new BasicNameValuePair("mobile", mobile));
                satParams.add(new BasicNameValuePair("city", city));
                satParams.add(new BasicNameValuePair("state", state));
                satParams.add(new BasicNameValuePair("country", country));
                satParams.add(new BasicNameValuePair("zip", zip));
                //for debugging..can be removed later
                System.out.println("satsangparams..........  " + satParams);
                System.out.println("satname for sp..........  " + satname);
                //
                 SAT_NAME = satname;
                edit.putString("satsang", satname);
               edit.commit();
                //for debugging..can be removed later
                System.out.println("after addparams satsangparams..........  " + SAT_NAME);
                //
                //Posting user data to PHP script /MySQL DB

                JSONObject json = jsonParser.makeHttpRequest(SATSANG_URL, "POST", satParams);
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    finish();
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

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            super.onPostExecute(s);


        }


        }

    }



