package jamstechs.com.gayathrijapamtracker;

//Created by sudhakar on 8/15/2015.

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Register extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener {
    JSONParser jsonparser = new JSONParser();
    Boolean isTrue;
    private HashMap<String, String> map = new HashMap<String, String>();
    private EditText user, pass, mobileno, emailid, cityname, statename, countryname, zipcode;
    private TextView tvsatsang;
    private Button mRegister, btnregsang;

    final static ArrayList<String> satsangID = new ArrayList<>();

    private ArrayList<HashMap<String, String>> mSatsangdata;

    private String tempStr;
    private String satname = "null";
    private String satsang;
    private ProgressDialog pDialog;
    private JSONArray mSatsang = null;
    private static final String TAG_NAME = "satsang_name";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_RESP = "response";
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String LOGIN_URL = "http://sageofkanchi.divinetraveller.net/register_transact.php";
    private static final String SATNAME_URL = "http://sageofkanchi.divinetraveller.net/get_satsang.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ZIP = "satsang_zip";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        //initialize
        user = (EditText) findViewById(R.id.edName);
        pass = (EditText) findViewById(R.id.edPwd);
        mobileno = (EditText) findViewById(R.id.edMobile);
        emailid = (EditText) findViewById(R.id.edEmail);
        tvsatsang = (TextView) findViewById(R.id.tvSatsang);
        cityname = (EditText) findViewById(R.id.edCity);
        statename = (EditText) findViewById(R.id.edState);
        countryname = (EditText) findViewById(R.id.edCountry);
        zipcode = (EditText) findViewById(R.id.edZip);

        //setting up buttons
        mRegister = (Button) findViewById(R.id.register);
        mRegister.setOnClickListener(this);
        btnregsang = (Button) findViewById(R.id.btnregsang);
        btnregsang.setOnClickListener(this);

        //run a class to grab satsang data for spinner
        new getSatsangDesc().execute("satsang_name");

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        tvsatsang.setText(satsangID.get(position));
        tempStr = satsangID.get(position);
        System.out.println("string satsang array temp str    " + tempStr);
        System.out.println(tempStr.substring(0, 6));
        satsang = tempStr;
        System.out.println(" spinner data.......  " + satsang);
        //making sure that item from spinner has been selected
        isTrue = true;

    }

    //redundant code?
    public void onNothingSelected(AdapterView<?> parent) {
        tvsatsang.setText("");
        System.out.println("nothing selected");
        Toast.makeText(getApplicationContext(), "nothing selected satsang" + satsang, Toast.LENGTH_LONG).show();

        System.out.println("varaiable from.  inside not selected.........  " + satsang);

    }


    //button clicked for Register
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                new CreateUser().execute();

                break;
            //user opts to add new satsangham
            case R.id.btnregsang:
                //make sure onClicklistener for selecting from spinner is set to false
                isTrue = false;
                Intent i = new Intent(this, SatsangRegister.class);
                startActivity(i);
                tvsatsang.setText("  ..");

                break;
            default:
                break;
        }

    }

    // New user creation
    class CreateUser extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;
            //getting data vrom the UI
            String username = user.getText().toString();
            String password = pass.getText().toString();
            String email = emailid.getText().toString();
            String mobile = mobileno.getText().toString();
            //when user had created new satsangh, value stored in that class is recalled
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Register.this);
            SharedPreferences.Editor editor = sp.edit();
            String satnameVal = sp.getString("satname", satname);
            //
            System.out.println("satsanginside Register.btnreg..........  " + satnameVal);

            String city = cityname.getText().toString();
            String state = statename.getText().toString();
            String country = countryname.getText().toString();
            String zip = zipcode.getText().toString();
            System.out.println("satNameval from Satsang activity.......  " + satnameVal);
            System.out.println("from spinner.......  " + satsang);

            //checking if the user has selected from spinner or from newly created Satsang data
            if (isTrue == true) {
                satsang = tempStr;
                System.out.println("from sp..true.....  " + satsang);
                System.out.println("clicked" + isTrue);
            } else {
                System.out.println("not clicked ..false" + isTrue);
                satsang = satnameVal;
                System.out.println("from spinner.......  " + satsang);

            }

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("mobile", mobile));
                params.add(new BasicNameValuePair("satsang", satsang));
                params.add(new BasicNameValuePair("city", city));
                params.add(new BasicNameValuePair("state", state));
                params.add(new BasicNameValuePair("zip", zip));
                params.add(new BasicNameValuePair("country", country));

                System.out.println("params..........  " + params);
                Log.d("request!", "starting");

                //Posting user data to PHP script /MySQL DB
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

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


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once user created
            pDialog.dismiss();
            tvsatsang.setText(satsang);
            if (file_url != null) {
                Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

    //this class offloads satsang details dynamically from the database just before the user registration starts.
    public class getSatsangDesc extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Gathering Satsang details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            //try to catch any exceptions:
            try {
                mSatsangdata = new ArrayList<HashMap<String, String>>();

                //Posting user data to script
                //THE IMPORTANT PART... I WAS STRUGGLING TO SEND DATA TO PHP ..WHEW...
                JSONObject json = jsonParser.getJSONFromUrl(SATNAME_URL);
                mSatsang = json.getJSONArray(TAG_POSTS);
                // looping through all posts according to the json object returned

                for (int i = 0; i < mSatsang.length(); i++) {
                    JSONObject c = mSatsang.getJSONObject(i);
                    //gets the content of each tag
                    //add to  array

                    //String satsangInfo = mSatsang.getJSONObject(i).toString();
                    String satsang_name = c.getString(TAG_NAME);
                    String satsang_zip = c.getString(TAG_ZIP);

                    // create list one and store values
                    //add the satsang details to the array to be used by the spinner
                    satsangID.add(satsang_name);

                }
                success = json.getInt(TAG_SUCCESS);

            } catch (JSONException e) {

                e.printStackTrace();
            }
            return String.valueOf(satsangID);

        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                //create the spinner
                Spinner spsatsang = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter aa = new ArrayAdapter<>(Register.this,
                        android.R.layout.simple_spinner_item, satsangID);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spsatsang.setAdapter(aa);
                spsatsang.setOnItemSelectedListener(Register.this);
                // Toast.makeText(Register.this, "post execute" + file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}